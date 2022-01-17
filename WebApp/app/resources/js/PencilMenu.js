import { Event, Observable } from "./utils/Observable.js";

function initMenu(menu) {
    initButtons(menu);
    initColors(menu);
}

function initButtons(menu) {
    menu.colorSelector = document.querySelector(".item.icon-color.green-selector");
    menu.colorSelector.addEventListener("click", menu.colorSelectorButtonClicked.bind(menu));
    menu.brushButton = document.querySelector(".item.icon-brush.active");
    menu.brushButton.addEventListener("click", menu.brushButtonClicked.bind(menu));
    menu.pencilButton = document.querySelector(".item.icon-pencil");
    menu.pencilButton.addEventListener("click", menu.pencilButtonClicked.bind(menu));
    menu.dropletButton = document.querySelector(".item.icon-droplet");
    menu.dropletButton.addEventListener("click", menu.dropletButtonClicked.bind(menu));
}

function initColors(menu) {
    menu.colorsValues = [];
    menu.colorsNames = ["green-selector", "yellow-selector", "red-selector", "cream-selector", "blue-selector", "gold-selector"];
    menu.colorNumber = 0;
    menu.colorsValues.push(getComputedStyle(document.documentElement).getPropertyValue("--canvas-green"));
    menu.colorsValues.push(getComputedStyle(document.documentElement).getPropertyValue("--canvas-yellow"));
    menu.colorsValues.push(getComputedStyle(document.documentElement).getPropertyValue("--canvas-red"));
    menu.colorsValues.push(getComputedStyle(document.documentElement).getPropertyValue("--canvas-cream"));
    menu.colorsValues.push(getComputedStyle(document.documentElement).getPropertyValue("--canvas-blue"));
    menu.colorsValues.push(getComputedStyle(document.documentElement).getPropertyValue("--canvas-gold"));
}


class PencilMenu extends Observable {

    constructor() {
        super();
        initMenu(this);
    }

    colorSelectorButtonClicked() {
        this.colorNumber += 1;
        if(this.colorNumber >= this.colorsValues.length){
            this.colorNumber = 0;
        }
        if(this.colorNumber === 0) {
            this.colorSelector.classList.remove(this.colorsNames[this.colorsNames.length-1]);
            this.colorSelector.classList.add(this.colorsNames[this.colorNumber]);
        }else {
            this.colorSelector.classList.remove(this.colorsNames[this.colorNumber-1]);
            this.colorSelector.classList.add(this.colorsNames[this.colorNumber]);
        }
        let event = new Event("colorChanged", this.colorsValues[this.colorNumber]);
        this.notifyAll(event);
    }

    brushButtonClicked() {
        let event = new Event("drawingToolChanged", 1);
        this.notifyAll(event);
        this.brushButton.classList.add("active");
        this.pencilButton.classList.remove("active");
        this.dropletButton.classList.remove("active");
    }

    pencilButtonClicked() {
        let event = new Event("drawingToolChanged", 2);
        this.notifyAll(event);
        this.pencilButton.classList.add("active");
        this.brushButton.classList.remove("active");
        this.dropletButton.classList.remove("active");
    }

    dropletButtonClicked() {
        let event = new Event("drawingToolChanged", 3);
        this.notifyAll(event);
        this.dropletButton.classList.add("active");
        this.pencilButton.classList.remove("active");
        this.brushButton.classList.remove("active");
    }
    
    
}

export default PencilMenu;