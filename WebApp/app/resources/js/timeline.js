import TimelineMenu from "./TimelineMenu"

var frameList = document.querySelector(".frames");

function initTimeline(timelineMenu){
    Timeline.timeline = document.getElementById(".timeline");
    initEvents(timelineMenu);
}

function initEvents(timelineMenu){
    timelineMenu.addEventlistener("deleteFrame", menu.deleteFrame.bind(timeline));
    timelineMenu.addEventlistener("timeSelected", menu.timeSelectedPressed.bind(timeline));
}

class Timeline {

    constructor(timelineMenu){
        initTimeline(this, timelineMenu);
    }

    deleteFrame(event){
        let li = document.getElementById(".data-id");
        frameList.removeChild(li);
    }
}
