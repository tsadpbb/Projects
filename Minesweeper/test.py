import tkinter as tk
import random as rr

class nastybutton:
    def __init__(self, x, y, master):
        self.button = tk.Button(master)
        self.button.grid(row=y,column=x)
        self.button.config(image=bomb[0], bd=0)
        self.button.bind('<Button-1>', self.leftclick)
        self.button.bind("<Button-2>", self.middleclick)
        self.button.bind("<Button-3>", self.rightclick)
        self.bombvalue = rr.randint(1, 5)
        self.x = x
        self.y = y
        self.pos = 16*self.x + self.y
        self.coor = (self.x, self.y)
        self.flag = False
        self.open = False
        self.near = 0

    def nearby(self, bombs):
        self.near = bombs

    def leftclick(self, event):
        if not self.open and not self.flag and weird.on:
            if self.bombvalue == 1:
                weird.fail()
                self.button.config(image=bomb[2])
                self.open = True
            else:
                weird.explode(self.pos)

    def middleclick(self, event):
        print(self)

    def rightclick(self, event):
        if weird.on:
            if not self.open:
                if not self.flag:
                    self.button.config(image=bomb[4])
                    self.flag = True
                    weird.mines.update(-1)
                else:
                    self.button.config(image=bomb[0])
                    self.flag = False
                    weird.mines.update(1)
    
    def __repr__(self):
        string = str([self.coor, self.flag, self.open, self.bombvalue, self.near])
        return string

class Counter:
    def __init__(self, side, master):
        if side == 'mine':
            make = 2
        else:
            make = 6
        self.count = 0
        self.one = tk.Label(master, image=count[0])
        self.one.grid(column=make, row=0)

        self.ten = tk.Label(master, image=count[0])
        self.ten.grid(column=(make-1), row=0)

        self.hun = tk.Label(master, image=count[0])
        self.hun.grid(column=(make-2), row=0)

    def update(self, value=0):
        self.count += value
        self.one.config(image=count[int(self.count%10)])
        self.ten.config(image=count[int(self.count%100//10)])
        self.hun.config(image=count[int(self.count//100)])

class Board(tk.Frame):
    def __init__(self, master):
        super().__init__(master)
        self.master = master
        self.grid()
        self.check = []
        self.on = True
        self.generateboard()
    
    def generateboard(self):
        self.mainframe = tk.Frame(self, bg='light grey')
        self.mainframe.columnconfigure(3, weight=1)
        self.mainframe.grid()

        self.face = tk.Button(self.mainframe,height=52,width=52,command=self.quit,image=faces[0],bd=0)
        self.face.grid(column=3)

        self.mines = Counter('mine', self.mainframe)
        self.time = Counter('time', self.mainframe)

        self.playtime()
        self.openarea()
        self.starttime()
        self.checkwin()

    def checkwin(self):
        if self.on:
            if self.mines.count == 0:
                win = True
                for block in self.check:
                    if block.bombvalue == 1 and not block.flag:
                        win = False
                if win:
                    self.on = False
                    self.face.config(image=faces[3])
                    for block in self.check:
                        if block.bombvalue != 1:
                            self.explode(block.pos)
            self.mainframe.after(1000, self.checkwin)

    def playtime(self):
        self.playarea = tk.Frame(self.mainframe)
        self.playarea.grid(columnspan=7)
        for x in range(30):
            for y in range(16):
                self.check.append(nastybutton(x, y, self.playarea))
        for block in self.check:
            self.checkmines(block.pos)

    def openarea(self):
        ls = []
        for block in self.check:
            if block.near == 0 and block.bombvalue != 1:
                ls.append(block)
        theblock = rr.choice(ls)
        self.explode(theblock.pos)

    def starttime(self):
        if self.on:
            self.time.update(1)
            self.mainframe.after(1000, self.starttime)

    def checkmines(self, pos):
        nearbomb = 0
        if self.checkvalue(2, pos+16, pos):
            nearbomb += 1
        if self.checkvalue(2, pos+1, pos):
            nearbomb += 1
        if self.checkvalue(2, pos-1, pos):
            nearbomb += 1
        if self.checkvalue(2, pos-16, pos):
            nearbomb += 1
        if self.checkvalue(2, pos+15, pos):
            nearbomb += 1
        if self.checkvalue(2, pos+17, pos):
            nearbomb += 1
        if self.checkvalue(2, pos-17, pos):
            nearbomb += 1
        if self.checkvalue(2, pos-15, pos):
            nearbomb += 1
        if self.check[pos].bombvalue == 1:
            self.mines.update(1)
        self.check[pos].nearby(nearbomb)

    def explode(self, pos):
        block = self.check[pos]
        if block.near == 0 and not block.flag and not block.open:
            block.open = True
            block.button.config(image=box[0])
            if self.checkvalue(1, pos+16, pos):
                self.explode(pos+16)
            if self.checkvalue(1, pos+1, pos):
                self.explode(pos+1)
            if self.checkvalue(1, pos-1, pos):
                self.explode(pos-1)
            if self.checkvalue(1, pos-16, pos):
                self.explode(pos-16)
            if self.checkvalue(1, pos+15, pos):
                self.explode(pos+15)
            if self.checkvalue(1, pos+17, pos):
                self.explode(pos+17)
            if self.checkvalue(1, pos-17, pos):
                self.explode(pos-17)
            if self.checkvalue(1, pos-15, pos):
                self.explode(pos-15)
        else:
            block.button.config(image=box[block.near])
            block.open = True
    
    def checkvalue(self, num, pos, posog):
        try:
            difx = self.check[pos].x - self.check[posog].x
            dify = self.check[pos].y - self.check[posog].y
            if -1 <= difx <= 1 and -1 <= dify <= 1:
                if num == 1:
                     return not self.check[pos].open
                elif num == 2:
                    if self.check[pos].bombvalue == 1:
                        return True
                else:
                    return False
            else:
                return False
        except IndexError:
            pass

    def fail(self):
        self.on = False
        self.face.config(image=faces[2])
        for block in self.check:
            if block.bombvalue != 1 and block.flag:
                block.button.config(image=bomb[3])
            elif block.bombvalue == 1 and not block.flag:
                block.button.config(image=bomb[1])

    def quit(self):
        self.on = False
        master.destroy()
        main()


def images():
    global faces, count, box, bomb
    faces = [
                tk.PhotoImage(file="images/facehappy.gif").zoom(2, 2),
                tk.PhotoImage(file="images/faceooo.gif").zoom(2, 2),
                tk.PhotoImage(file="images/facedead.gif").zoom(2, 2),
                tk.PhotoImage(file="images/facewin.gif").zoom(2, 2)
                ]

    count = [
                tk.PhotoImage(file="images/count0.gif").zoom(2, 2),
                tk.PhotoImage(file="images/count1.gif").zoom(2, 2),
                tk.PhotoImage(file="images/count2.gif").zoom(2, 2),
                tk.PhotoImage(file="images/count3.gif").zoom(2, 2),
                tk.PhotoImage(file="images/count4.gif").zoom(2, 2),
                tk.PhotoImage(file="images/count5.gif").zoom(2, 2),
                tk.PhotoImage(file="images/count6.gif").zoom(2, 2),
                tk.PhotoImage(file="images/count7.gif").zoom(2, 2),
                tk.PhotoImage(file="images/count8.gif").zoom(2, 2),
                tk.PhotoImage(file="images/count9.gif").zoom(2, 2),
                tk.PhotoImage(file="images/countdash.gif").zoom(2, 2)
                ]
    box = [    
                tk.PhotoImage(file="images/box0.gif").zoom(2, 2),
                tk.PhotoImage(file="images/box1.gif").zoom(2, 2),
                tk.PhotoImage(file="images/box2.gif").zoom(2, 2),
                tk.PhotoImage(file="images/box3.gif").zoom(2, 2),
                tk.PhotoImage(file="images/box4.gif").zoom(2, 2),
                tk.PhotoImage(file="images/box5.gif").zoom(2, 2),
                tk.PhotoImage(file="images/box6.gif").zoom(2, 2),
                tk.PhotoImage(file="images/box7.gif").zoom(2, 2),
                tk.PhotoImage(file="images/box8.gif").zoom(2, 2)
                ]
    bomb = [   
                tk.PhotoImage(file="images/nonclicked.gif").zoom(2,2),
                tk.PhotoImage(file="images/bomb.gif").zoom(2,2),
                tk.PhotoImage(file="images/bombdead.gif").zoom(2,2),
                tk.PhotoImage(file="images/bombwrong.gif").zoom(2,2),
                tk.PhotoImage(file="images/flag.gif").zoom(2,2)
                ]

def main():
    global master, weird
    master = tk.Tk()
    master.title('Minesweeper by Dylan')
    images()
    weird = Board(master=master)
    master.mainloop()

main()
