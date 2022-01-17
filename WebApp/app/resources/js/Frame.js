import { Event, Observable } from "./utils/Observable.js";

class Frame extends Observable{

    constructor(img) {
        super();
        this.element = Frame.createFrameElement(img);
        this.id = Date.now().toString();
        this.deleteStatus = false;
        this.timeClicked = 0;
        this.ms = 100;
        this.element.querySelector(".frame-list").setAttribute("data-id", this.id);
        this.deleteButton = this.element.querySelector(".icon-trash.trash");
        this.deleteButton.addEventListener("click", this.deleteFrameClicked.bind(this));
        this.timeButton = this.element.querySelector(".time-selector");
        this.timeButton.addEventListener("click", this.timeButtonClicked.bind(this));
    }

    getFrame() {
        return this.element;
    }

    deleteFrameClicked() {
        this.deleteStatus = true;
        let event = new Event("deleteFrame", this.id);
        this.notifyAll(event);
    }

    timeButtonClicked() {
        if(this.timeClicked == 3){
            this.timeClicked = 0;
        }else{
            this.timeClicked++;
        }
        this.items = this.timeButton.getElementsByTagName("span");

        this.items[this.timeClicked].classList.add("selected");
        this.items[(this.timeClicked == 0) ? (3) : (this.timeClicked-1)].classList.remove("selected");
        switch(this.timeClicked) {
            case 0:
                this.ms = 100;
                break;
            case 1:
                this.ms = 200;
                break;
            case 2:
                this.ms = 500;
                break;
            case 3:
                this.ms = 1000;
                break;
            default:
                this.ms = 1000;
        }
    }

    static createFrameElement(img) {
        let temp = document.getElementById("frame-template");
        var clon = temp.content.cloneNode(true);
        clon.querySelector("img").src = img;
        return clon;
    }
}

export default Frame;