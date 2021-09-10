and08395 : Dylan Anderson
swedz015 : Josh Swedzinski

Josh worked on main, generate boats, display, and print.
Dylan worked on main, constructor, fire cannon, fire missile, and drone search.

Simply compile and run with "javac Main.java && java Main" and enjoy!

We have one bug and it has to deal with detecting when a ship is sunk.
The way it works is it determines how many numbers of the ship hit are on the board
and uses modulus to determine if it's the last of of a ship of that kind of ship.
It works when there's only one ship of that kind but it can be confusing when there are multiples ships of the same kind.
So far, based on how people work with battle ship, when they get a hit they try to continue hitting that ship.
They hit around it. As long as multiple ships of the same kind are clumped together, the modulus thing should work because
A person wouldn't normally hit a ship and then do some random place next and hit a ship of the same kind.
That would increase the amount of turns taken and it would go agaisnt the goal of the game.
This becomes an actual issue when it comes to missiles.
