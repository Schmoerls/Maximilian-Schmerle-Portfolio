import { Event, Observable } from "./utils/Observable.js";

function initMenu(menu) {
    initButtons(menu);
}

function initButtons(menu) {
    menu.undoButton = document.querySelector(".item.icon-undo");
    menu.undoButton.addEventListener("click", menu.undoButtonPressed.bind(menu));
    menu.saveButton = document.querySelector(".item.icon-save");
    menu.saveButton.addEventListener("click", menu.saveButtonPressed.bind(menu));
    menu.deleteButton = document.querySelector(".item.icon-trash");
    menu.deleteButton.addEventListener("click", menu.deleteButtonPressed.bind(menu));
    menu.createGifButton = document.querySelector(".item.icon-video");
    menu.createGifButton.addEventListener("click", menu.createGifButtonPressed.bind(menu));
}

class CanvasMenu extends Observable {

    constructor() {
        super();
        initMenu(this);
    }

    undoButtonPressed() {
        let event = new Event("undo");
        this.notifyAll(event);
    }

    saveButtonPressed() {
        let event = new Event("save");
        this.notifyAll(event);
    }

    deleteButtonPressed() {
        let event = new Event("delete");
        this.notifyAll(event);
    }

    createGifButtonPressed() {
        let event = new Event("create");
        this.notifyAll(event);
    }

}

export default CanvasMenu;