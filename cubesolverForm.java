import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.sql.SQLException;
import java.util.Calendar;

/**
 * Created by Pam on 4/19/2017.
 */
public class cubesolverForm extends JFrame {
    private JPanel rootPanel;
    private JTextField SolverTextField;
    private JTextField SolvedTextField;
    private JLabel SolverJLabel;
    private JLabel SolvedJLabel;
    private JButton addSolverButton;
    private JButton DeleteSolverButton;
    private JButton quitButton;
    private JTable resultsTable;

    cubesolverForm(final CubeSolverModel cubesolverTableModel){

        setContentPane(rootPanel);
        pack();
        setTitle("Rubik's Cube Solver Application");
        addWindowListener((WindowListener) this);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        resultsTable.setGridColor(Color.BLACK);
        resultsTable.setModel(CubeSolverModel);

        addSolverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //Get Movie title, make sure it's not blank
                String solverName = SolverTextField.getText();

                if (solverName == null || solverName.trim().equals("")) {
                    JOptionPane.showMessageDialog(rootPane, "Please enter a name for the solver");
                    return;
                }

                //Get movie year. Check it's a number between 1900 and present year
                double solvedTime;

                    solvedTime = Integer.parseInt(SolvedTextField.getText());

                    System.out.println("Adding " + solverName + " " + solvedTime);
                    boolean insertedRow = cubesolverTableModel.insertRow(solverName, solvedTime);

                    if (!insertedRow) {
                        JOptionPane.showMessageDialog(rootPane, "Error adding new solver");
                    }
                    // If insertedRow is true and the data was added, it should show up in the table, so no need for confirmation message.

            }

        });
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    cubesolverDB.shutdown();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                System.exit(0);   //Should probably be a call back to Main class so all the System.exit(0) calls are in one place.
            }
        });

        DeleteSolverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int currentRow = resultsTable.getSelectedRow();

                if (currentRow == -1) {      // -1 means no row is selected. Display error message.
                    JOptionPane.showMessageDialog(rootPane, "Please choose a movie to delete");
                }
                boolean deleted = cubesolverTableModel.deleteRow(currentRow);
                if (deleted) {
                    try {
                        cubesolverDB.loadAllSolvers();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(rootPane, "Error deleting movie");
                }
            }
        });
    }

}
