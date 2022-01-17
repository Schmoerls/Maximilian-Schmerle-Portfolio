import PencilMenu from "./PencilMenu.js";
import Canvas from "./Canvas.js";
import CanvasMenu from "./CanvasMenu.js";

var pencilMenu,
    canvas,
    canvasMenu
    
function init() {
  console.log("### Starting Christmas Card Generator ###");
  pencilMenu = new PencilMenu();
  canvasMenu = new CanvasMenu();
  canvas = new Canvas(pencilMenu, canvasMenu);
}

init();