package prgms;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Quiz extends JFrame implements ActionListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JLabel qLabel, rewardLabel;
    JRadioButton A, B, C, D;
    JButton nextBtn, audienceBtn, fiftyBtn;
    ButtonGroup bg;

    int question = 1;
    int reward = 0;

   
    int audienceUsed = 0;
    int fiftyUsed = 0;

    String correctAnswer = "";
    String wa="";
    String playerName = "";

    public Quiz() {

        playerName = JOptionPane.showInputDialog(null, "Enter your name:");

        setTitle("Samantha Quiz - " + playerName);
        setSize(1120, 750);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Background Color
        getContentPane().setBackground(new Color(240, 240, 255));

        qLabel = new JLabel();
        qLabel.setBounds(20, 20, 600, 40);
        qLabel.setFont(new Font("Arial", Font.BOLD, 18));
        qLabel.setForeground(new Color(60, 0, 120));
        add(qLabel);

        rewardLabel = new JLabel("Reward: ₹0");
        rewardLabel.setBounds(480, 20, 200, 30);
        rewardLabel.setFont(new Font("Arial", Font.BOLD, 14));
        rewardLabel.setForeground(new Color(0, 128, 0));
        add(rewardLabel);

        // Options
        A = new JRadioButton();
        B = new JRadioButton();
        C = new JRadioButton();
        D = new JRadioButton();

        JRadioButton[] arr = {A, B, C, D};
        int y = 100;
        for (JRadioButton r : arr) {
            r.setBounds(40, y, 400, 25);
            r.setBackground(new Color(240, 240, 255));
            r.setForeground(Color.DARK_GRAY);
            y += 40;
            add(r);
        }

        bg = new ButtonGroup();
        bg.add(A); bg.add(B); bg.add(C); bg.add(D);

        // Buttons with colors
        audienceBtn = new JButton("Audience");
        audienceBtn.setBounds(40, 300, 120, 35);
        audienceBtn.setBackground(new Color(255, 204, 102));
        audienceBtn.addActionListener(this);
        add(audienceBtn);

        fiftyBtn = new JButton("50-50");
        fiftyBtn.setBounds(180, 300, 120, 35);
        fiftyBtn.setBackground(new Color(102, 204, 255));
        fiftyBtn.addActionListener(this);
        add(fiftyBtn);

        nextBtn = new JButton("Next");
        nextBtn.setBounds(330, 300, 120, 35);
        nextBtn.setBackground(new Color(144, 238, 144));
        nextBtn.addActionListener(this);
        add(nextBtn);

        loadQuestion();
        setVisible(true);
    }

    // Load questions
    void loadQuestion() {

        if (question == 10) {
            audienceBtn.setEnabled(false);
            fiftyBtn.setEnabled(false);
        }

        switch (question) {

            case 1:
                qLabel.setText("Q1) Samantha made her acting debut with which Telugu film?");
                A.setText("Attarintiki Daredi");
                B.setText("Ye Maaya Chesave");
                C.setText("Brindavanam");
                D.setText("Majili");
                correctAnswer = "B";
                wa="C";
                break;

            case 2:
                qLabel.setText("Q2) Which award has Samantha won multiple times?");
                A.setText("Filmfare Award");
                B.setText("Grammy Award");
                C.setText("National Geography Award");
                D.setText("Booker Prize");
                correctAnswer = "A";
                wa="C";
                break;

            case 3:
                qLabel.setText("Q3) Samantha founded which non-profit organization?");
                A.setText("New Hope Foundation");
                B.setText("Pratyusha Support");
                C.setText("Smile India Trust");
                D.setText("Helping Hearts");
                correctAnswer = "B";
                wa="C";
                break;

            case 4:
                qLabel.setText("Q4) Samantha acted with Vijay Deverakonda in?");
                A.setText("Theri");
                B.setText("Mersal");
                C.setText("Khushi");
                D.setText("24");
                correctAnswer = "C";
                wa="A";
                break;

            case 5:
                qLabel.setText("Q5) Samantha’s workout videos are mostly about?");
                A.setText("Dance Training");
                B.setText("Strength & Fitness");
                C.setText("Cooking Recipes");
                D.setText("Stand-up Comedy");
                correctAnswer = "B";
                wa="C";
                break;

            case 6:
                qLabel.setText("Q6) Samantha played ‘Raji’ in?");
                A.setText("The Family Man 2");
                B.setText("Mirzapur");
                C.setText("Asur");
                D.setText("Farzi");
                correctAnswer = "A";
                wa="C";
                break;

            case 7:
                qLabel.setText("Q7) Romantic drama starring Samantha?");
                A.setText("Robot 2.0");
                B.setText("Eega");
                C.setText("Rangasthalam");
                D.setText("Majili");
                correctAnswer = "D";
                wa="A";
                break;

            case 8:
                qLabel.setText("Q8) Samantha’s dog name?");
                A.setText("Bailey");
                B.setText("Snoopy");
                C.setText("Hash");
                D.setText("Rocky");
                correctAnswer = "C";
                wa="A";
                break;

            case 9:
                qLabel.setText("Q9) Samantha is known for ______ style.");
                A.setText("Simple & Elegant");
                B.setText("Sci-fi Costume");
                C.setText("Traditional Only");
                D.setText("Cosplay");
                correctAnswer = "A";
                wa="D";
                break;

            case 10:
                qLabel.setText("Q10) Samantha acted as a journalist in?");
                A.setText("U-Turn");
                B.setText("Dookudu");
                C.setText("Kaala");
                D.setText("Saaho");
                correctAnswer = "A";
                break;
        }

        bg.clearSelection();
    }

    // Find selected option
    String getSelectedOption() {
        if (A.isSelected()) return "A";
        if (B.isSelected()) return "B";
        if (C.isSelected()) return "C";
        if (D.isSelected()) return "D";
        return "";
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == audienceBtn && audienceUsed == 0 && question != 10) {
            audienceUsed = 1;
            reward -= 50;
            JOptionPane.showMessageDialog(this, "Audience Suggests: " + correctAnswer);
        }

        if (e.getSource() == fiftyBtn && fiftyUsed == 0 && question != 10) {
            fiftyUsed = 1;
            reward -= 50;
            JOptionPane.showMessageDialog(this, "50-50 Lifeline Used!\nCorrect option + one wrong option remain."+correctAnswer+" "+wa);
        }

        if (e.getSource() == nextBtn) {

            String selected = getSelectedOption();

            if (selected.equals("")) {
                JOptionPane.showMessageDialog(this, "Please select an option!");
                return;
            }

            if (selected.equalsIgnoreCase(correctAnswer)) {
                reward += 100;
            } else {
                JOptionPane.showMessageDialog(this, "Wrong Answer! Game Over!");
                System.exit(0);
            }

            rewardLabel.setText("Reward: ₹" + reward);

            question++;

            if (question > 10) {
                JOptionPane.showMessageDialog(this, "Final Reward: ₹" + reward);
                System.exit(0);
            }

            loadQuestion();
        }
    }

    public static void main(String[] args) {
        new Quiz();
    }
}