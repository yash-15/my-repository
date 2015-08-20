##Move: A Brain Shifting Puzzle
This program solves the puzzle *'Move: A Brain Shifting Puzzle'* available on [Google Playstore] (https://play.google.com/store/apps/details?id=com.nitako.move&hl=en). It has 1,000,000+ downloads!

###Input Format

First line contains the size of the puzzle (n) (atmost 5).

Next n lines describe the grid as follows:

  Each line consists of n characters describing that row as:

    '.' if the box is of black color
    'X' if the box is blocked i.e. grey color
    'a'-'z' otherwise i.e. destination box, selecting a unique letter for each color.

Next n lines describe the tokens as follows:

  Each line consists of n characters describing that row as:

    '.' if the box has no token
    'a'-'z' otherwise, with using the same map between letter and color.

    
The result is not affected by entering any blank lines.


###Sample Input (Pack 2 Level 2)

```
3

.X.
bXX
.rb

b..
r..
b..
```
