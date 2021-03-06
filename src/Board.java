import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created with IntelliJ IDEA.
 * User: Tom
 * Class: Board
 * Description: Instantiates the board, for a game of tic tac toe, and allows both the user and computer to interact with it easily.
 * Date: 12/2/13
 * Time: 5:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class Board implements ActionListener, ComponentListener {

    private Computer c;


    private JFrame frame;

    //Creates a list on 9 JButtons, these will be clicked by the User to decide where to move
    private static JButton[] buttons = new JButton[9];

    //Creates the variable turn, and initializes it to false. Whenever the turn is false it's the players turn.
    //Soooo, the player always has the first turn.
    private boolean turn;

    //statuslbl, is the Status Label for the game, it displays who's turn it is. Who won, and if it's a cats game.
    private JLabel statuslbl = new JLabel();

    private int turnCount;

    private int buttonFontStart;
    private int labelFontStart;

    //Play again is a hidden JButton that appears only once the game has been deemed over, which resets all the JButtons to "" and the turnCount back to 0.
    private JButton playagain = new JButton("Play Again");


    //Becomes true once there is a cats game, so the computer doesn't try to move again.
    private boolean gameOver = false;
    private Dimension[] startsizes = new Dimension[12];
    private Point[] startLocations = new Point[12];

    public static int resizeCounter = 0;
    public static double startWidth;
    public static double startHeight;
    public static double currentWidth;
    public static double currentHeight;


    public Board()
    {
        c = new Computer(this);
        CreateGUI();
        WhoFirst();





    }

    private void WhoFirst()
    {
        Point invisLocation = frame.getLocation();
        Dimension invisSize = frame.getSize();
        frame.setVisible(false);
        int choice;
        Dimension size = frame.getPreferredSize();
        //No .OnTop for JOptionPane, so if the frame is larger than a certain size select player as the box would fall behind the screen
        String[] choices = new String[2];
        choices[0] = "Player";
        choices[1] = "Computer";
        choice = JOptionPane.showOptionDialog(null, //Component parentComponent
                "Who will go first?", //Object message,
                "Choose an option", //String title
                JOptionPane.YES_NO_OPTION, //int optionType
                JOptionPane.INFORMATION_MESSAGE, //int messageType
                null, //Icon icon,
                choices, //Object[] options,
                "Player");//Object initialValue
        frame.setVisible(true);
        frame.setLocation(invisLocation);
        frame.setSize(invisSize);


        if (choice == 0)
        {
            turn = false;
            turnCount = 0;
            statuslbl.setText("Player's Turn!");

        }
        else if (choice == -1)
        {

            //Closes the Jframe when the X is hit on the Dialog box
            WindowEvent wev = new WindowEvent(frame, WindowEvent.WINDOW_CLOSING);
            Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);


        }

        else
        {
            turn = true;
            turnCount = 1;

            c.computerTurn();
            statuslbl.setText("Player's Turn!");


        }
    }

    public int getTurnCount()
    {
        return turnCount;
    }


    //turnComplete changes the turn and adds to the turnCount, called whenever a move is made on the board.
    private void turnComplete()
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

    public void resize (double x,double y)
    {


        double multiplier = 0;
        if (x < y)
            multiplier = x;
        else
            multiplier = y;
        for (int i = 0; i < 9; i++)
        {

            buttons[i].setSize((new Dimension((int) ((double)startsizes[i].getWidth() * x), (int) ((double)startsizes[i].getHeight() * y))));
            buttons[i].setLocation((new Point((int) (startLocations[i].getX() * x), (int)(startLocations[i].getY() * y))));

            buttons[i].setFont(new Font("Dialog",Font.PLAIN, (int) ((double)buttonFontStart * multiplier )));

        }
        playagain.setSize(new Dimension( (int) (startsizes[9].getWidth() * x), (int) (startsizes[9].getHeight() * y)));
        playagain.setLocation(new Point((int) (startLocations[9].getX() * x), (int) (startLocations[9].getY() * y)));

        statuslbl.setSize(new Dimension((int) (startsizes[10].getWidth() * x), (int) (startsizes[10].getHeight() * y)));
        statuslbl.setLocation(new Point((int) (startLocations[10].getX() * x), (int) (startLocations[10].getY() * y)));

        playagain.setFont(new Font("Dialog",Font.PLAIN, (int) ((double)labelFontStart * multiplier )));
        statuslbl.setFont(new Font("Dialog",Font.PLAIN, (int) ((double)labelFontStart * multiplier )));

    }


    @Override
    public void componentResized(ComponentEvent e) {
        if (resizeCounter == 0)
        {
            startWidth = frame.getPreferredSize().getWidth();
            startHeight = frame.getPreferredSize().getHeight();
            resizeCounter++;
        }

        currentHeight = frame.getHeight();

        currentWidth = frame.getWidth();
        //System.out.println(currentWidth + ", " + currentHeight);

        resize( currentWidth / startWidth, (currentHeight) / startHeight);
        frame.setPreferredSize(new Dimension((int)currentWidth, (int)currentHeight));
       // CreateGUI();


    }



      



    public void CreateGUI()
    {
        //Creates a new JFrame and sets its dimensions to 800,600 the size of the Tic Tac Toe board Image
        frame = new JFrame("Tic Tac Toe");
        frame.addComponentListener(this);
        frame.setPreferredSize(new Dimension(800, 600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setResizable(true);
        frame.setLayout(null);
        frame.setAlwaysOnTop(true);




        Font ButtonFont = new Font("Dialog", Font.PLAIN, 80);
        buttonFontStart = 80;
        Font LabelFont = new Font("Dialog", Font.PLAIN, 20);
        labelFontStart = 20;

        statuslbl.setFont(LabelFont);

        //Sets the location and visibility of the play again button, which is invisible until the game ends
        playagain.setVisible(false);



        //a for loop to construct 9 Jbuttons and set the font to large and add actionlisteners that call actionPerformed() when clicked
        for (int i = 0; i < 9; i++)
        {
            buttons[i] = new JButton("");
            buttons[i].setFont(ButtonFont);
            buttons[i].addActionListener(this);
            // System.out.println(buttons[i]);
            int eachWidth = (int)(.441 * (double) (frame.getPreferredSize().getWidth()));
            //System.out.println(frame.getPreferredSize().getWidth());

            buttons[i].setSize(new Dimension((int) (.3441 *  frame.getPreferredSize().getWidth() - 30), (int) (.4 * frame.getPreferredSize().getHeight() - 100 )));
            startsizes[i] = buttons[i].getSize();
           // System.out.println(buttons[i]);
        }

        //Adds an ActionListener to playAgain and sets the bounds of each button. Bounds were found by guessing & checking
        playagain.addActionListener(this);
        playagain.setLocation(0,0);
        playagain.setSize(200,50);
        statuslbl.setBounds(350,0,300,50);
        statuslbl.setLocation(350,0);
        statuslbl.setSize(300,50);
        startLocations[9] = playagain.getLocation();
        startLocations[10] = statuslbl.getLocation();
        startsizes[9] = playagain.getSize();
        startsizes[10] = statuslbl.getSize();

        buttons[0].setLocation(10, 50);
        buttons[1].setLocation(283, 50);
        buttons[2].setLocation(556, 50);
        buttons[3].setLocation(10, 228);
        buttons[4].setLocation(283, 228);
        buttons[5].setLocation(556, 228);
        buttons[6].setLocation(10, 405);
        buttons[7].setLocation(283, 405);
        buttons[8].setLocation(556, 405);


        //For loop adds each large Jbutton to the ContentPane, these will be the buttons that the user clicks during his turn
        for (int i = 0; i < 9; i++)
        {
            startLocations[i] = buttons[i].getLocation();
            frame.getContentPane().add(buttons[i]);
        }



        //adds the play again button, status label to the Jframe
        frame.getContentPane().add(playagain);
        frame.getContentPane().add(statuslbl);



        //Packs the frame and makes it visible
        frame.pack();
        //frame.setVisible(true);


    }




    public void actionPerformed(ActionEvent evt)
    {

        //Gets the source of the object and casts it to a button then passes the button to Clicked to perform an action on the board
        Object src = evt.getSource();
        JButton clicked = (JButton) src;
        clicked(clicked);


    }

    public void clicked(JButton button)
    {

        //This for loop finds the index of the button that was clicked and sets it to noMove so the computer
        //knows which button was just clicked


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



    //// All Previous methods were building the UI, or Interacting with it, the following methods check for victory, and reset the game board! ////






    private boolean ThreeEqual(int[] ls)
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

    private void CatsGame()
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
            statuslbl.setText("It's a Tie!");
            playagain.setVisible(true);
            gameOver = true;
        }
    }


    private void CheckVictory()
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
                    statuslbl.setText("Player Wins, Congratulations!");
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

    //This method resets the buttons, counter, statuslbl, and gameOver.
    private void reset()
    {
        for (int i = 0; i < 9; i++)
        {
            buttons[i].setText("");

        }
        gameOver = false;
        c.resetDiag();
        playagain.setVisible(false);
        WhoFirst();



    }













    public void componentMoved(ComponentEvent e)
    {

    }
    public void componentHidden(ComponentEvent e)
    {

    }
    public void componentShown(ComponentEvent e)
    {

    }




}
