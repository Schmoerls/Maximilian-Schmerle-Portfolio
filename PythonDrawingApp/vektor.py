from tkinter import *
from tkinter.colorchooser import askcolor
import numpy as np
from numpy import matrix as M
from math import sin, cos, pi


canvas_width = 1500
canvas_height = 800

xStart = 0
yStart = 0
rectanglesPresent = []
whatToDraw = 1

polysNumber = 0
polygons = []
newPolyControllPoints = []
polygonsControlPoints = []
polyLinesPresent = []
polyStarted = False
firstX = 0
firstY = 0
selectedPolyPoint = 0
polyFillPoints = []

selectedPoint = 0

linesNumbers = 0
lines = []
controllPoints = []

selectedColor = ["Black", "Black"]

def createLine(xStart, xEnd, yStart, yEnd, poly):
    global linesNumbers
    vertical = False
    differenceX = abs(xEnd - xStart)
    differnceY = abs(yEnd - yStart)
    
    if( differnceY > differenceX):
        vertical = True
        xStart, yStart = yStart, xStart
        xEnd, yEnd = yEnd, xEnd

    if ( xStart > xEnd):
        xStart, xEnd = xEnd, xStart
        yStart, yEnd = yEnd, yStart

    value = (yEnd-yStart) / (xEnd-xStart)
    t = yStart - value * xStart
    
    for x in range(xStart, xEnd+1):
        y = int(value * x + t)
        if vertical:
            x, y = y, x
        if(poly == False):
            rect = w.create_rectangle(x, y, x, y, outline=selectedColor[1], tags="Line")
        else:
            rect = w.create_rectangle(x, y, x, y, outline=selectedColor[1], tags=["Poly", polysNumber])
        rectanglesPresent.append(rect)


def curveItInMan():
    tags = w.gettags(selectedPoint)
    line = lines[int(tags[1])]
    stepValue = 1 / len(lines[int(tags[1])])
    t = 0
    x1, y1, x2, y2 = w.coords(selectedPoint)
    e1, f1, e2, f2 = w.coords(line[0])
    g1, h1, g2, h2 = w.coords(line[-1])
    p0 = [e1, f1]
    p1 = [x1, y1]
    p2 = [g1, h1]
    for i in range(1, len(lines[int(tags[1])])-1):
        t += stepValue
        newX = (1-t)*(1-t)*p0[0]+2*t*(1-t)*p1[0]+t*t*p2[0]
        newY = (1-t)*(1-t)*p0[1]+2*t*(1-t)*p1[1]+t*t*p2[1]
        selPoint = line[i]
        w.coords(selPoint, newX, newY, newX, newY)

def translate(tx, ty, p=None):
    T = M([[1, 0, tx],
           [0, 1, ty],
           [0, 0, 1]])
    if p is None:
        return T
    else:
        p = list(p)
        p.append(1)
        p = T @ p
        x = p.tolist()[0][0]
        y = p.tolist()[0][1]
        return((x,y))

def rotate(angle, p=None):
    R = M([[cos(angle), -sin(angle), 0],
          [sin(angle), cos(angle), 0],
          [0, 0, 1]])
    if p is None:
        return R
    else:
        p = list(p)
        p.append(1)
        p = R @ p
        x = p.tolist()[0][0]
        y = p.tolist()[0][1]
        return((x,y))

def angle(event, point, lin, poly):
    selectedPoint = point
    tags = w.gettags(selectedPoint)
    line = lin
    
    if(poly == False):
        if(int(tags[3]) == 1):
            p1X, p1Y, c, b = w.coords(line[0])
            p2X, p2Y, d, e = w.coords(line[-1])
            p3X, p3Y, f, g = event.x, event.y, event.x, event.y
            toCenterX, toCenterY, a, h = w.coords(line[-1])
        else:
            p1X, p1Y, c, b = event.x, event.y, event.x, event.y
            p2X, p2Y, d, e = w.coords(line[0])
            p3X, p3Y, f, g = w.coords(line[-1])
            toCenterX, toCenterY, a, h = w.coords(line[0])
    else:
        p1X, p1Y, c, b = w.coords(line[0])
        p2X, p2Y, d, e = w.coords(line[-1])
        p3X, p3Y, f, g = event.x, event.y, event.x, event.y
        toCenterX, toCenterY, a, h = w.coords(line[-1])

    vector1X = p1X - p2X
    vector1Y = p1Y - p2Y
    vector2X = p3X - p2X
    vector2Y = p3Y - p2Y

    x = np.array([vector1X, vector1Y])
    y = np.array([vector2X, vector2Y])

    dot = np.dot(x, y)
    x_modulus = np.sqrt((x*x) .sum())
    y_modulus = np.sqrt((y*y) .sum())
    cos_angle = dot / x_modulus / y_modulus
    angle = np.arccos(cos_angle)

    if(poly == False):
        if(int(tags[3]) == 1):
            cross1 = np.cross(x, y)
        else:
            cross1 = np.cross(y, x)
    else:
        cross1 = np.cross(x, y)

    if(cross1 < 0):
        angle = angle * (-1)

    if(poly == False):
        for i in lines[int(tags[1])]:
            x1, y1, x2, y2 = w.coords(i)
            firstX, firstY = translate(-toCenterX, -toCenterY, (x1, y1))
            newX, newY = rotate(angle, (firstX, firstY))
            x, y = translate(toCenterX, toCenterY, (newX, newY))
            w.coords(i, x, y, x, y)
        pointArray = controllPoints[int(tags[1])]
        bezierPoint = pointArray[2]
        x1, y1, x2, y2 = w.coords(bezierPoint)
        firstX, firstY = translate(-toCenterX, -toCenterY, (x1, y1))
        newX, newY = rotate(angle, (firstX, firstY))
        x, y = translate(toCenterX, toCenterY, (newX, newY))
        w.coords(bezierPoint, x, y, x+4, y+4) 
    else:
        for i in line:
            x1, y1, x2, y2 = w.coords(i)
            firstX, firstY = translate(-toCenterX, -toCenterY, (x1, y1))
            newX, newY = rotate(angle, (firstX, firstY))
            x, y = translate(toCenterX, toCenterY, (newX, newY))
            w.coords(i, x, y, x, y)  

def pressed( event ):
    global polyStarted
    global xStart 
    global yStart 
    global firstX
    global firstY
    global rectanglesPresent

    global selectedPoint
    global selectedPolyPoint

    x1 = event.x -10
    x2 = event.x +10
    y1 = event.y -10
    y2 = event.y +10

    # Suchen nach Controll Points
    for i in w.find_withtag("Point"):
        cx1, cy1, cx2, cy2 = w.coords(i)
        if( cx1 > x1 and cy1 > y1 and cx2 < x2 and cy2 < y2 ):
            selectedPoint = i
            break

    for i in w.find_withtag("PolyPoint"):
        cx1, cy1, cx2, cy2 = w.coords(i)
        if( cx1 > x1 and cy1 > y1 and cx2 < x2 and cy2 < y2 ):
            selectedPolyPoint = i
            break

    # Festlegen der Startparameter, falls keine Controll Points gefunden wurden
    if(selectedPoint == 0 and selectedPolyPoint == 0):
        if(whatToDraw == 1):
            xStart = event.x
            yStart = event.y
        else:
            if(polyStarted == False):
                polyStarted = True
                xStart = event.x
                yStart = event.y
                firstX = xStart
                firstY = yStart
            else:
                createLine(xStart, event.x, yStart, event.y, True)
                polyLinesPresent.append(rectanglesPresent)
                createPolyControllPoints(xStart, yStart)
                rectanglesPresent = []
                xStart = event.x
                yStart = event.y

# Methode die aufgerufen wird, wenn Space gedrückt wird
def polyFinished(event):
    global polyStarted
    global polyLinesPresent
    global polysNumber
    global newPolyControllPoints
    global rectanglesPresent

    createLine(xStart, firstX, yStart, firstY, True)
    polyLinesPresent.append(rectanglesPresent)
    polygons.append(polyLinesPresent)

    createPolyControllPoints(xStart, yStart)
    polygonsControlPoints.append(newPolyControllPoints)

    fillPoly()

    rectanglesPresent = []
    polyLinesPresent = []
    newPolyControllPoints = []
    polyStarted = False
    polysNumber += 1

vertices = []
globalEdgeTabel = []
activeTable = []
parity = True
whichLine = 0
polyActiveFillPoints = []

def fillPoly():
    global activeTable
    global globalEdgeTabel
    global vertices
    global parity
    global whichLine
    global polyActiveFillPoints
    setUpVertices()
    initGlobalEdgeTable()
    #print(globalEdgeTabel)
    filterGlobalEdgeTable()
    sortGlobalEdgeTable = sorted(globalEdgeTabel, key=lambda x: (x[0], x[2], x[1]))
    setActiveTable(sortGlobalEdgeTable)
    for i in range(sortGlobalEdgeTable[0][0], sortGlobalEdgeTable[-1][1]):
        filling()
        checking(sortGlobalEdgeTable)
        activeTable = sorted(activeTable, key=lambda x: (x[1]))
    vertices = []
    globalEdgeTabel = []
    activeTable = []
    parity = True
    whichLine = 0

    tags = w.gettags(newPolyControllPoints[0])
    if(polysNumber != int(tags[1])):
        polyFillPoints[int(tags[1])] = polyActiveFillPoints
    else:
        polyFillPoints.append(polyActiveFillPoints)
    polyActiveFillPoints = []
    

def setUpVertices():
    for i in newPolyControllPoints:
        vertice = []
        x1, y1, x2, y2 = w.coords(i)
        vertice.append(int(x1+2))
        vertice.append(int(y1+2))
        vertices.append(vertice)


def initGlobalEdgeTable():
    for i in range(len(vertices)):
        output = []
        firstEdge = vertices[i]
        secondEdge = vertices[0 if i == len(vertices)-1 else i+1]
        if(firstEdge[1] <= secondEdge[1]):
            if(firstEdge[1] != secondEdge[1]):
                output.append(firstEdge[1])
                output.append(secondEdge[1])
                output.append(firstEdge[0])
            else:
                output.append(firstEdge[1])
                output.append(secondEdge[1])
                if(firstEdge[0] <= secondEdge[0]):
                    output.append(firstEdge[0])
                else:
                    output.append(secondEdge[0])
        else:
            output.append(secondEdge[1])
            output.append(firstEdge[1])
            output.append(secondEdge[0])
        if((secondEdge[1] - firstEdge[1]) == 0):
            output.append(100)
        else:
            slope = (secondEdge[0]-firstEdge[0])/(secondEdge[1]-firstEdge[1])
            output.append(slope)
        globalEdgeTabel.append(output)

def filterGlobalEdgeTable():
    for i in range(len(globalEdgeTabel)):
        #print("RUUN")
        if(globalEdgeTabel[i][3] == 100):
            del globalEdgeTabel[i]
        #print("Len= "+str(len(globalEdgeTabel)))
        #print("i= "+str(i))
        if(len(globalEdgeTabel)-1 <= i):
            break

def setActiveTable(table):
    global whichLine
    for i in range(len(table)):
        if(i == 0):
            whichLine = table[i][0]
            activeTable.append(table[i][1:4])
        else:
            if(table[i][0] == whichLine):
                activeTable.append(table[i][1:4])

def filling():
    global parity
    global whichLine
    parity = True
    for i in range(len(activeTable)-1):
        if(parity):
            rect = w.create_rectangle(int(activeTable[i][1]), whichLine, int(activeTable[i+1][1]), whichLine, outline=selectedColor[1], tags="Line")
            polyActiveFillPoints.append(rect)
            parity = False
        else:
            parity = True
    #print("NEW")
    for j  in range(len(activeTable)):
        activeTable[j][1] = activeTable[j][1]+activeTable[j][2]
        #print(activeTable[j][1])
    whichLine += 1

def checking(table):
    global activeTable
    result=[]
    for i in range(len(activeTable)):
        if(activeTable[i][0] != whichLine):
            result.append(activeTable[i])
    for i in range(len(table)):
        if(table[i][0] == whichLine):
            result.append(table[i][1:4])
    activeTable = result


# Linien Controll Points erstellen
def createControllPoints():
    createdControllPoints = []
    mid = int((len(rectanglesPresent) +1)/ 2)
    x1, y1, x2, y2 = w.coords(rectanglesPresent[0])
    x3, y3, x4, y4 = w.coords(rectanglesPresent[-1])
    x5, y5, x6, y6 = w.coords(rectanglesPresent[mid])
    rect = w.create_rectangle(x1-2, y1-2, x1+2, y1+2, fill="Black", tags=["Point", linesNumbers, "Control", 1])
    createdControllPoints.append(rect)

    rect = w.create_rectangle(x3-2, y3-2, x3+2, y3+2, fill="Black", tags=["Point", linesNumbers, "Control", 2])
    createdControllPoints.append(rect)

    rect = w.create_rectangle(x5-2, y5-2, x5+2, y5+2, fill="Black", tags=["Point", linesNumbers, "Bezier"])
    createdControllPoints.append(rect)

    controllPoints.append(createdControllPoints)

# Polly Controll Point erstellen
def createPolyControllPoints(x1, y1):
    rect = w.create_rectangle(x1-2, y1-2, x1+2, y1+2, fill="Black", tags=["PolyPoint", polysNumber, len(newPolyControllPoints)])
    newPolyControllPoints.append(rect)

def released( event ):
    global selectedPoint
    global selectedPolyPoint
    global linesNumbers
    global newPolyControllPoints
    # kein Controll Points ausgewählt
    if(selectedPoint == 0 and selectedPolyPoint == 0):
        # Linie wird gezeichnet und die Variablen resetet
        if(whatToDraw == 1):
            global rectanglesPresent
            xEnd = event.x
            yEnd = event.y
            global xStart
            global yStart
            delete()
            rectanglesPresent = []
            createLine(xStart, xEnd, yStart, yEnd, False)
            createControllPoints()
            lines.append(rectanglesPresent)
            rectanglesPresent = []
            linesNumbers += 1
    # Controll Points ausgewählt -> Reset
    else:
        if(selectedPolyPoint != 0):
            tags = w.gettags(newPolyControllPoints[0])
            for x in polyFillPoints[int(tags[1])]:
                w.delete(x)
            fillPoly()
            newPolyControllPoints = []
        selectedPoint = 0
        selectedPolyPoint = 0

def delete():
    for x in rectanglesPresent:
        w.delete(x)

def motion( event ):
    global rectanglesPresent
    global newPolyControllPoints
    # Linie wird gezeichnet
    if(selectedPoint == 0 and selectedPolyPoint == 0):
        if(whatToDraw == 1):
            xEnd = event.x
            yEnd = event.y
            global xStart
            global yStart
            delete()
            rectanglesPresent = []
            createLine(xStart, xEnd, yStart, yEnd, False)
    # Controll Point wurde ausgewählt
    else:
        # Linien Controll Point geklickt
        if(selectedPoint != 0):
            tags = w.gettags(selectedPoint)
            # End Controll Point ausgewählt
            if(tags[2] == "Control"):
                selectedLine = lines[int(tags[1])]
                angle(event, selectedPoint, selectedLine, False)
                if(tags[3] == "1"):
                    x1, y1, x2, y2 = w.coords(selectedLine[0])
                else:
                    x1, y1, x2, y2 = w.coords(selectedLine[-1])
                w.coords(selectedPoint, x1-2, y1-2, x1+2, y1+2)
            # Bezier Controll Point ausgewählt
            else:
                w.coords(selectedPoint, event.x-2, event.y-2, event.x+2, event.y+2)
                curveItInMan()
        # Polygon Controll Point geklickt
        else:
            tags = w.gettags(selectedPolyPoint)
            polygon = polygons[int(tags[1])]
            controllPointsList = polygonsControlPoints[int(tags[1])]
            first = int(tags[2])-1
            second = int(tags[2])+1
            if(first < 0):
                first = len(polygon)-1
            if(second >= len(polygon)):
                second = 0
            x1, y1, x2, y2 = w.coords(controllPointsList[first])
            x3, y3, x4, y4 = w.coords(controllPointsList[second])
            deleteLine(polygon[first])
            deleteLine(polygon[int(tags[2])])
            rectanglesPresent = []
            createLine(int(x1), event.x, int(y1), event.y, True)
            polygon[first] = rectanglesPresent
            rectanglesPresent = []
            createLine(int(x3), event.x, int(y3), event.y, True)
            polygon[int(tags[2])] = rectanglesPresent
            rectanglesPresent = []
            w.coords(selectedPolyPoint, event.x-2, event.y-2, event.x+2, event.y+2)
            newPolyControllPoints = controllPointsList

def deleteLine(line):
    for x in line:
        w.delete(x)

def lineButtonClicked():
    global whatToDraw
    whatToDraw = 1
    buttonLine.configure(bg="green")
    buttonPolygon.configure(bg="white")

def polygonButtonClicked():
    global whatToDraw
    whatToDraw = 2
    buttonLine.configure(bg="white")
    buttonPolygon.configure(bg="green")

xStart2 = 0
yStart2 = 0
allRects = []

def pressed2(event):
    global xStart2
    global yStart2
    xStart2 = event.x
    yStart2 = event.y
    setAllRects()

def setAllRects():
    global allRects
    allRects = []
    for p in polygons:
        for l in p:
            for r in l:
                allRects.append(r)

    for p in polygonsControlPoints:
        for r in p:
            allRects.append(r)

    for l in lines:
        for r in l:
            allRects.append(r)

    for p in controllPoints:
        for r in p:
            allRects.append(r)

    for p in polyFillPoints:
        for r in p:
            allRects.append(r)
    for re in rectangles:
        for r in re:
            allRects.append(r) 
    for c in circles:
        for r in c:
            allRects.append(r)

def motion2(event):
    global xStart2
    global yStart2
    moveX = (event.x - xStart2) / 5
    moveY = (event.y - yStart2) / 5
    for i in allRects:
        x1, y1, x2, y2 = w.coords(i)
        x1New, y1New = translate(moveX, moveY, (x1, y1))
        x2New, y2New = translate(moveX, moveY, (x2, y2))
        w.coords(i, x1New, y1New, x2New, y2New)
    xStart2 = event.x
    yStart2 = event.y

def changeColor():
    global selectedColor
    selectedColor = askcolor(title="Tkinter Color Chooser")
    print(selectedColor)
    print(selectedColor[1])

def deleteAll():
    global polygons
    global polygonsControlPoints
    global polyFillPoints
    global lines
    global controllPoints
    global polysNumber
    global linesNumbers
    global rectangles
    global circles

    setAllRects()
    for r in allRects:
        w.delete(r)
    polygons = []
    polygonsControlPoints = []
    polyFillPoints = []
    lines = []
    controllPoints = []
    polysNumber = 0
    linesNumbers = 0
    rectangles = []
    circles = []

CPEnabled = True

def disableEnableCP(event):
    global CPEnabled
    if(CPEnabled):
        for p in polygonsControlPoints:
            for r in p:
                w.itemconfigure(r, state="hidden")
        for p in controllPoints:
            for r in p:
                w.itemconfigure(r, state="hidden")
        CPEnabled = False
    else:
        for p in polygonsControlPoints:
            for r in p:
                w.itemconfigure(r, state="normal")
        for p in controllPoints:
            for r in p:
                w.itemconfigure(r, state="normal")
        CPEnabled = True

rectangleMode = True

def rectangleButtonClicked():
    global rectangleMode
    rectangleMode = True
    buttonRectangle.configure(bg="green")
    buttonCircle.configure(bg="white")

def circleButtonClicked():
    global rectangleMode
    rectangleMode = False
    buttonRectangle.configure(bg="white")
    buttonCircle.configure(bg="green")

def pressed3(event):
    global xStart 
    xStart = event.x
    global yStart
    yStart = event.y

def motion3(event):
    global rectangle
    global circle
    xEnd = event.x
    yEnd = event.y
    if(rectangleMode):
        for r in rectangle:
            w.delete(r)
        rectangle = []
        createRect(xStart, yStart, xEnd, yEnd)
    else:
        for r in circle:
            w.delete(r)
        circle = []
        createCircle(xStart, yStart, xEnd, yEnd)

def released3(event):
    global rectangle
    global circle
    if(rectangleMode):
        createRect(xStart, yStart, event.x, event.y)
        rectangles.append(rectangle)
        rectangle = []
    else:
        createCircle(xStart, yStart, event.x, event.y)
        circles.append(circle)
        circle = []

rectangle = []
rectangles = []

circle = []
circles = []

def createCircle(x1, y1, x2, y2):
    xM = x1 + (x2 - x1) / 2
    yM = y1 + (y2 - y1) / 2
    m = [xM, yM]
    r = (abs(x1 - x2)) / 2
    for i in range(0,720):
        x = r * cos(i*0.5) + m[0]
        y = r * sin(i*0.5) + m[1]
        rect = w.create_rectangle(x, y, x, y, outline=selectedColor[1])
        circle.append(rect)

def createRect(x1, y1, x2, y2):
    global rectangle
    width = abs(x1 - x2)
    height = abs(y1 - y2)
    point1 = [x1, y1]
    point2 = [x2, y2]
    point3 = [x2, y1]
    point4 = [x1, y2]
    if(point1[0] < point2[0]):
        startX = point1[0]
    else:
        startX = point2[0]
    if(point1[1] < point2[1]):
        startY = point1[1]
    else:
        startY = point2[1]
    for i in range(width):
        rect = w.create_rectangle(startX, point1[1], startX, point1[1], outline=selectedColor[1])
        rectangle.append(rect)
        rect = w.create_rectangle(startX, point2[1], startX, point2[1], outline=selectedColor[1])
        rectangle.append(rect)
        startX += 1
    for j in range(height):
        rect = w.create_rectangle(point1[0], startY, point1[0], startY, outline=selectedColor[1])
        rectangle.append(rect)
        rect = w.create_rectangle(point2[0], startY, point2[0], startY, outline=selectedColor[1])
        rectangle.append(rect)
        startY += 1

master = Tk()
master.title( "Paint for the poor" )
w = Canvas(master, 
           width=canvas_width, 
           height=canvas_height)
w.pack(expand = YES, fill = BOTH)
w.bind( "<Button-1>", pressed )
w.bind( "<ButtonRelease-1>", released )
w.bind( "<B1-Motion>", motion )
master.bind( 'x', disableEnableCP )
master.bind("<space>", polyFinished)
w.bind( "<Button-2>", pressed2 )
w.bind( "<B2-Motion>", motion2 )
w.bind( "<Button-3>", pressed3 )
w.bind( "<B3-Motion>", motion3 )
w.bind( "<ButtonRelease-3>", released3 )


message = Label( master, text = "Press and Drag the mouse to draw a line + In Polymode Press the mouse multiple times to set the edges and finish the poly with space" )
message.pack( side = TOP )

buttonFrame = Frame(master)
buttonFrame.pack( side = BOTTOM)
setColorButton = Button(
                    buttonFrame,
                    text='Select a Color',
                    command=changeColor, 
                    width=10, 
                    height=3)
setColorButton.pack( side=LEFT )
buttonDeleteAll = Button(buttonFrame, text="Delete", width=10, height=3, command=deleteAll)
buttonDeleteAll.pack( side=LEFT )
buttonLine = Button(buttonFrame, text="Line", width=10, bg="green", height=3, command=lineButtonClicked)
buttonLine.pack( side=LEFT )
buttonRectangle = Button(buttonFrame, highlightbackground='black', bg="green", text="Rectangle", width=10, height=3, command=rectangleButtonClicked)
buttonRectangle.pack( side=RIGHT )
buttonCircle = Button(buttonFrame, highlightbackground='black', text="Circle", width=10, height=3, command=circleButtonClicked)
buttonCircle.pack( side=RIGHT )
buttonPolygon = Button(buttonFrame, text="Polygon", width=10, height=3, command=polygonButtonClicked)
buttonPolygon.pack( side=RIGHT )
    
mainloop()