import Frame from "./Frame.js";

function initCanvas(canvas, pencilMenu, canvasMenu) {
    canvas.canvas = document.getElementById("canvas");
    canvas.c = canvas.canvas.getContext("2d");
    canvas.c.fillStyle = "#fff";
    canvas.c.fillRect(0, 0, canvas.canvas.width, canvas.canvas.height);
    canvas.draw_color = getComputedStyle(document.documentElement).getPropertyValue("--canvas-green");
    canvas.safeColor = canvas.draw_color;
    canvas.draw_width = "10";
    canvas.paint = false;
    canvas.mouseX, canvas.mouseY = 0;
    canvas.lastX, canvas.lastY = 0;

    canvas.values = [];
    canvas.valueCounter = 0;
    initEvents(canvas, pencilMenu, canvasMenu);
}

function initEvents(canvas, pencilMenu, canvasMenu, timelineMenu) {
    window.addEventListener('load', () => {
        canvas.canvas.addEventListener("mousedown", canvas.start.bind(canvas));
        canvas.canvas.addEventListener("mousemove", canvas.draw.bind(canvas));
        canvas.canvas.addEventListener("mouseup", canvas.stop.bind(canvas));      
    });
    pencilMenu.addEventListener("drawingToolChanged", canvas.drawingToolChanged.bind(canvas));
    pencilMenu.addEventListener("colorChanged", canvas.colorChanged.bind(canvas));

    canvasMenu.addEventListener("undo", canvas.undoPressed.bind(canvas));
    canvasMenu.addEventListener("save", canvas.savePressed.bind(canvas));
    canvasMenu.addEventListener("delete", canvas.deletePressed.bind(canvas));
    canvasMenu.addEventListener("create", canvas.createPressed.bind(canvas));
}

function getX2(event, canvas) {
    var rect = canvas.canvas.getBoundingClientRect();
    return (event.clientX - rect.left) / (rect.right - rect.left) * canvas.canvas.width;
}

function getY2(event, canvas) {
    var rect = canvas.canvas.getBoundingClientRect();
    return (event.clientY - rect.top) / (rect.bottom - rect.top) * canvas.canvas.height ;
}

function undoLastLine(canvas) {
    if(canvas.valueCounter != 0) {
        canvas.values.splice(canvas.values.length-1, 1);
        canvas.valueCounter--;
        canvas.c.clearRect(0, 0, canvas.canvas.width, canvas.canvas.height);
        canvas.c.fillRect(0, 0, canvas.canvas.width, canvas.canvas.height);
        redrawCanvas(canvas);
    }
    
}

function redrawCanvas(canvas) {
    canvas.values.forEach(element => {
        canvas.c.beginPath();
        element.forEach(el => {
            canvas.c.lineTo(el[0], el[1]);
            canvas.c.strokeStyle = canvas.draw_color;
            canvas.c.lineWidth = canvas.draw_width;
            canvas.c.lineCap = "round";
            canvas.c.lineJoin = "round";
            canvas.c.stroke();
        });
        canvas.c.stroke();
        canvas.c.closePath();
    });
}

class Canvas {

    constructor(pencilMenu, canvasMenu) {
        this.frames = [];
        initCanvas(this, pencilMenu, canvasMenu);
    }

    start(event){
        this.paint = true;
        this.values.push([]);
        this.c.beginPath();
    }

    draw(event){
        if (this.paint){
            this.c.lineTo(getX2(event, this), getY2(event, this));
            this.values[this.valueCounter].push([getX2(event, this), getY2(event, this)]);
            this.c.strokeStyle = this.draw_color;
            this.c.lineWidth = this.draw_width;
            this.c.lineCap = "round";
            this.c.lineJoin = "round";
            this.c.stroke();
        }
    }

    stop(event){
        if (this.paint){
            this.c.stroke();
            this.c.closePath();
            this.paint = false;
        }
        this.valueCounter++
    }

    drawingToolChanged(event) {
        if(event.data === 1){
            this.erase = false;
            this.draw_width = 10;
            this.draw_color = this.safeColor;
        } else if(event.data === 2){
            this.erase = false;
            this.draw_width = 5;
            this.draw_color = this.safeColor;
        } else if(event.data === 3){
            this.erase = true;
            this.draw_color = 'white';
        }
    }

    colorChanged(event) {
        if(this.erase) {
            this.safeColor = event.data;
        } else {
            this.draw_color = event.data;
            this.safeColor = event.data;
        }
    }

    undoPressed(event) {
        undoLastLine(this);
    }

    savePressed(event) {
        let frameList = document.querySelector(".frames");
        let img = this.canvas.toDataURL();
        let frame = new Frame(img)
        frame.addEventListener("deleteFrame", this.deleteFrame.bind(this));
        frameList.appendChild(frame.getFrame());
        this.frames.push(frame);
    }

    deletePressed(event) {
        this.c.clearRect(0, 0, this.canvas.width, this.canvas.height);
        this.values.splice(0, this.values.length);
        this.valueCounter = 0;
        this.c.fillRect(0, 0, this.canvas.width, this.canvas.height);
    }

    deleteFrame(event) {
        this.frameList = document.querySelector(".frames");
        this.frameList.querySelector("[data-id=\"" + event.data + "\"]").remove();
        for (let x = 0; x < this.frames.length; x++){
            if(this.frames[x].id == event.date){
                this.frames.splice(x, 1);
            }
        }
    }

    createPressed(event) {
        var gif = new GIF({
            workers: 2,
            quality: 10,
            workerScript: "./vendors/gif.js/gif.worker.js"
          });
          
          this.frameList = document.querySelector(".frames");
          this.items = this.frameList.getElementsByTagName("img");
          for (let x=0; x < this.items.length; x++){
            let img = new Image();
            img.src = this.items[x].src;
            gif.addFrame(img, {delay: this.frames[x].ms});
          }
          
          gif.on('finished', function(blob) {
            window.open(URL.createObjectURL(blob));
          });
        gif.render();          
    }
}

export default Canvas;
