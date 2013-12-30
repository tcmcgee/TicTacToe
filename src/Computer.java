import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Tom
 * Date: 12/2/13
 * Time: 5:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class Computer {
    public static Board b;


    public Random generator = new Random();

    //Two variables for the two diagnals, it's crucial that these are eliminated ASAP as most possibilities of two victories
    //Comes from diagonals
    private boolean diagOne = false;
    private boolean diagTwo = false;

    public Computer(Board b)
    {
        this.b = b;

    }

    public void resetDiag()
    {
        diagOne = false;
        diagTwo = false;
    }





    public void computerTurn()
    {
        //another array of all possible wins
        int[][] PossibleWins ={{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}};


        int move = -1;


        //calls check move on each possible win, referencing the board, if a move is found break the for loop and continue.
        for (int i=0; i < 8; i++)
        {
            move = CheckComputerWin(PossibleWins[i][0], PossibleWins[i][1], PossibleWins[i][2]);
            if (move != -1)
                break;

        }

        //If checkMoves doesn't have a required move, checks if checkmoves2 does
        if (move == -1)
        {


            for (int i=0; i < 8; i++)
            {
                move = CheckPlayerWin(PossibleWins[i][0], PossibleWins[i][1], PossibleWins[i][2]);
                if (move != -1)
                    break;

            }
        }
        //If the move is -1 or noMove checks to see if diagonals still need to be blocked, if they do, blocks them accordingly
        //Otherwise call the randomMove() fn
        if (move == -1 /*|| move == b.getNoMove()*/)
        {
            if (diagOne && diagTwo)
                move = randomMove();
            else
            {
                move = Diagn();
            }
        }

        int mymove = move;
        b.clicked(b.getButton(mymove));
    }




    //CheckComputerWin evaluates the current board and prioritizes a computer victory over blocking a player
    private int CheckComputerWin(int x, int y, int z)
    {

        //Stops top left
        if (b.getTurnCount() == 5 && b.getButtonTXT(1) == "X" && b.getButtonTXT(6) == "X" && b.getButtonTXT(5) == "X")
            return 8;


        //Mose special cases were added after playing against the prior bot, so are very hard coded

        //If it's the first turn and the player didn't move to the center, the computer always takes the center
        if (b.getTurnCount() == 1 && b.getButtonTXT(4) != "X")
        {
            return 4;
        }

        //Checks to see if the computer moved to the center square, checks if it's the third turn, then has to see if the player has moved
        //into the bottom row, if they have block them accordingly, otherwise prevent the 3 corners tactic.
        else if (b.getButtonTXT(4) == "O" && b.getTurnCount() == 3 && b.getButtonTXT(3) == "" && (b.getButtonTXT(6) == "X" || b.getButtonTXT(7) == "X" || b.getButtonTXT(8) == "X" ))
        {
            if (b.getButtonTXT(5) == "X")
                return 2;
            else if (b.getButtonTXT(8) == "X" && b.getButtonTXT(6) == "X"   )
            {
                return 7;
            }
            else if (b.getButtonTXT(8) == "X" && b.getButtonTXT(7) == "X")
            {
                return 6;

            }
            else if (b.getButtonTXT(7) == "X" && b.getButtonTXT(6) == "X")
            {
                return 8;
            }
            else if (b.getButtonTXT(0) == "X" && b.getButtonTXT(6) == "X")
                return 3;
            else
                return 5;
        }

        //Checks to see if the middle is 'O', and if it's the second turn and the middle right is open and they moved to in the bottom right
        //added because they were the only way I beat the computer prior to them
        else if (b.getButtonTXT(4) == "O" && b.getTurnCount() == 3 && b.getButtonTXT(5) == "" && b.getButtonTXT(8) == "X")
        {
            if (b.getButtonTXT(3) == "X")
                return 0;
            else
                return 5;
        }
        else if (b.getButtonTXT(5) == "X" &&  b.getTurnCount() == 3 && b.getButtonTXT(7) == "X" && b.getButtonTXT(8) == "")
        {
            return 8;
        }

        //The following cases go through each possible way to win, and if there are two of the same, the computer automatically blocks it, but in the way the Ifs are ordered
        //the computer prioritizes victory for itself, before blocking the opponent
        //If there's no obvious move, returns -1
        else if ((b.getButtonTXT(x).equals(b.getButtonTXT(y))) && b.getButtonTXT(x) != "" && b.getButtonTXT(y) != "" && b.getButtonTXT(z) == ""  && b.getButtonTXT(y) != "X")
        {
            return z;
        }
        else if((b.getButtonTXT(x).equals(b.getButtonTXT(z)))&& b.getButtonTXT(x) != "" && b.getButtonTXT(z) != ""  && b.getButtonTXT(y) == ""  && b.getButtonTXT(z) != "X")
        {
            return y;
        }
        else if((b.getButtonTXT(y).equals(b.getButtonTXT(z)))&& b.getButtonTXT(y) != "" && b.getButtonTXT(z) != ""  && b.getButtonTXT(x) == ""  && b.getButtonTXT(y) != "X")
        {
            return x;
        }
        else
            return -1;

    }


    //Is called if the computer can't win and the player needs to be blocked
    private int CheckPlayerWin(int x, int y, int z)
    {
    if ((b.getButtonTXT(x).equals(b.getButtonTXT(y))) && b.getButtonTXT(x) != "" && b.getButtonTXT(y) != "" && b.getButtonTXT(z) == "" && b.getButtonTXT(y) != "O")
    {
        return z;
    }
    else if((b.getButtonTXT(x).equals(b.getButtonTXT(z)))&& b.getButtonTXT(x) != "" && b.getButtonTXT(z) != ""  && b.getButtonTXT(y) == ""  && b.getButtonTXT(x) != "O")
    {
        return y;
    }
    else if((b.getButtonTXT(y).equals(b.getButtonTXT(z)))&& b.getButtonTXT(y) != "" && b.getButtonTXT(z) != ""  && b.getButtonTXT(x) == ""  && b.getButtonTXT(y) != "O")
    {
        return x;
    }
    else
        return -1;
    }




    private int randomMove()
    {
        String moveTurn = "O";
        int randy = -5;
        while (moveTurn != "")
        {
            randy = generator.nextInt(9);
            moveTurn = b.getButtonTXT(randy);

        }
        return randy;
    }



    //Diagn, checks the diagonals, as they're the trickiest locations (in my opinion) when it comes to making an AI unbeatable
    //Diagn is only called if CheckComputerWin doesn't return a higher priority move.
    private int Diagn()
    {

        if (b.getButtonTXT(0) == "" && diagOne == false)
        {
            diagOne = true;
            return 0;
        }
        else if (b.getButtonTXT(8) == "" && diagOne == false)
        {
            diagOne = true;
            return 8;
        }
        else if (b.getButtonTXT(6) == "" && diagTwo == false)
        {
            diagTwo = true;
            return 6;
        }
        else if (b.getButtonTXT(2) == "" && diagTwo == false)
        {
            diagTwo = true;
            return 2;
        }

        else
            return randomMove();
    }




}
