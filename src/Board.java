import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: Tom
 * Class: Board
 * Description: Instantiates the board, for a game of tic tac toe, and allows both the user and computer to interact with it easily.
 * Date: 12/2/13
 * Time: 5:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class Board implements ActionListener{

    private Computer c;

    //Creates a list on 9 JButtons, these will be clicked by the User to decide where to move
    private JButton[] buttons = new JButton[9];

    //Creates the variable turn, and initializes it to false. Whenever the turn is false it's the players turn.
    //Soooo, the player always has the first turn.
    private boolean turn = false;

    //statuslbl, is the Status Label for the game, it displays who's turn it is. Who won, and if it's a cats game.
    private JLabel statuslbl = new JLabel("Player's Turn!");

    //noMove is a variable created on the fly, as the Computer was apparently moving faster than the computer could
    //set the text of the players move, so it is simply off limits to the computer's use and is initialized as -5, a value the computer would never use.
    private int noMove = -5;

    private int turnCount = 0;

    //Play again is a hidden JButton that appears only once the game has been deemed over, which resets all the JButtons to "" and the turnCount back to 0.
    private JButton playagain = new JButton("Play Again");


    //Becomes true once there is a cats game, so the computer doesn't try to move again.
    private boolean gameOver = false;


    public Board()
    {
        c = new Computer(this);

    }

    public int getTurnCount()
    {
        return turnCount;
    }

    public int getNoMove()
    {
        return noMove;
    }




    //turnComplete changes the turn and adds to the turnCount, called whenever a move is made on the board.
    public void turnComplete()
    {
        turn = !turn;
        turnCount++;

    }


     //Returns the JButton at the given index, useful Method for the computer who has to pass a JButton to clicked
    public JButton getButton(int i)
    {
        return buttons[i];
    }

    //Overloaded the getButtonTXT method since the clicked function will need to pass a button
    //And most of the computers functions will only have the int.
    public String getButtonTXT(int i)
    {
        return buttons[i].getText();
    }
    public String getButtonTXT(JButton b)
    {
        return b.getText();
    }






    //Sets the button to either "X" or "O" depending on the turn,
    public void setButton(JButton b, boolean turn)
    {
        if (b.getText() != "" )
        {
            System.out.println("Please try again, you can't move there!");
        }
        else if (turn)
        {
            b.setText("O");
            turnComplete();
        }


        //If it's the computers turn and not a cats game already, the computer moves.
        else if (!turn)
        {
            b.setText("X");
            turnComplete();
            CatsGame();
            if (gameOver == false)
            {
                c.computerTurn();
            }

        }


    }




    public void CreateGUI()
    {
        //Creates a new JFrame and sets its dimensions to 800,600 the size of the Tic Tac Toe board Image
        JFrame frame = new JFrame("Tic Tac Toe");
        frame.setPreferredSize(new Dimension(800,600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        //Creates two fonts one to be used for the statuslbl the other to be used for each Jbutton
        Font large = new Font("Dialog", Font.PLAIN, 80);
        Font small = new Font("Dialog", Font.PLAIN, 20);



        //JButton button = new JButton("TEST");

        //Setting the layout to null allows me to place each element easier with .setBounds();
        frame.setLayout(null);

        //Uploads the Image included in the src file of a Tic Tac Toe board. And sets it's coordinates to (0,0) and size to 800 x 600
        ImageIcon board_path = new ImageIcon("src/TicTacToe.jpg") ;
        JLabel board = new JLabel(board_path);
        board.setBounds(0,0,800,600);


        statuslbl.setFont(small);


        //Sets the location and visibility of the play again button, which is invisible until the game ends
        playagain.setVisible(false);
        playagain.setBounds(0,0,200,50);



        //a for loop to construct 9 Jbuttons and set the font to large and add actionlisteners that call actionPerformed() when clicked
        for (int i = 0; i < 9; i++)
        {
            buttons[i] = new JButton("");
            buttons[i].setFont(large);
            buttons[i].addActionListener(this);
            // System.out.println(buttons[i]);
        }

        //Adds an ActionListener to playAgain and sets the bounds of each button. Bounds were found by guessing & checking
        playagain.addActionListener(this);
        statuslbl.setBounds(350,0,300,50);
        buttons[0].setBounds(0,50,265,150);
        buttons[1].setBounds(273,50,265,150);
        buttons[2].setBounds(546,50,265,150);
        buttons[3].setBounds(0,205,265,190);
        buttons[4].setBounds(273,205,265,190);
        buttons[5].setBounds(546,205,265,190);
        buttons[6].setBounds(0,405,265,190);
        buttons[7].setBounds(273,405,265,190);
        buttons[8].setBounds(546,405,265,190);

        //For loop adds each large Jbutton to the ContentPane, these will be the buttons that the user clicks during his turn
        for (int i = 0; i < 9; i++)
        {
            frame.getContentPane().add(buttons[i]);
        }



        //adds the play again button, status label, and the image of the Tic Tac Toe board.
        frame.getContentPane().add(playagain);
        frame.getContentPane().add(statuslbl);
        frame.getContentPane().add(board);


        //Packs the frame and makes it visible
        frame.pack();
        frame.setVisible(true);


    }


    //This method resets the buttons, counter, statuslbl, and gameOver.
    public void reset()
    {
        for (int i = 0; i < 9; i++)
        {
            buttons[i].setText("");

        }
        turn = false;
        statuslbl.setText("Player's Turn!");
        turnCount = 0;
        gameOver = false;
        c.resetDiag();
        playagain.setVisible(false);

    }


    public void clicked(JButton button)
    {

        //This for loop finds the index of the button that was clicked and sets it to noMove so the computer
        //knows which button was just clicked
        for (int i = 0; i < 9; i ++)
        {
            if (button.equals(buttons[i]))
            {
                noMove = i;
            }
        }


        if (button.getText().equals("Play Again"))
        {
            reset();
        }
        else if (gameOver == true)
        {

        }
        else
        {
            setButton(button, turn);
            CheckVictory();
        }





    }

    public void actionPerformed(ActionEvent evt) {

        //Gets the source of the object and casts it to a button then passes the button to Clicked to perform an action on the board
        Object src = evt.getSource();
        JButton clicked = (JButton) src;
        clicked(clicked);


    }


    public boolean ThreeEqual(int[] ls)
    {
        //Checks if the buttons at the index of the 3 int list are all the same.
        if ((getButtonTXT(ls[0]) != "") &&  (getButtonTXT(ls[1]) != "") && (getButtonTXT(ls[2]) != ""))
        {

            return ((getButtonTXT(ls[0]).equals(getButtonTXT(ls[1])) &&  (getButtonTXT(ls[0]).equals(getButtonTXT(ls[2])))));
        }
        else
        {
            return false;
        }

    }

    public void CatsGame()
    {

        //Cycles through each button, if the button isn't "" adds 1, if the counter reaches 9 it's a cats game.
        int counter = 0;
        for(int i = 0; i < 9; i++)
        {
            if (getButtonTXT(i) != "")
            {
                counter++;
            }
        }
        if (counter == 9)
        {
            statuslbl.setText("Cat's Game!");
            playagain.setVisible(true);
            gameOver = true;
        }
    }


    public void CheckVictory()
    {
        //A 2D array of all possible wins
        int[][] PossibleWins ={{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}};

        //A for loop that goes through each of 8 possible wins
        for (int i = 0; i < 8; i++)
        {

            //Creates a list try one which is 1D and has 3 elements, one for each possible button that must be pressed to win
            int[] tryOne ={PossibleWins[i][0], PossibleWins[i][1], PossibleWins[i][2]};

            //If there's 3 buttons that are a possible win, Change the statuslbl correctly.
            if (ThreeEqual(tryOne))
            {
                if ((getButtonTXT(PossibleWins[i][0]).equals("X")))
                {
                    playagain.setVisible(true);
                    gameOver = true;
                    statuslbl.setText("This Program is broken!");
                }
                if ((getButtonTXT(PossibleWins[i][0]).equals("O")))
                {
                    playagain.setVisible(true);
                    gameOver = true;
                    statuslbl.setText("Computer Wins, great effort!");

                }
            }


        }
        //If checkVictory didn't already determine there to be a victor, checks if it's a cats game.
        if (playagain.isVisible() == false)
            CatsGame();


    }




}
