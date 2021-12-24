package checkers;

import bots.RandomBot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * SidePanel implements the side bar of the CheckersApp
 *
 * @author Arastun Mammadli
 */
public class SidePanel implements ActionListener {
    // panel: main panel of the SidePanel
    public JPanel panel = new JPanel();

    // evaluation: displays Board position evaluation
    private final JLabel evaluation = new JLabel("", SwingConstants.CENTER);
    // whiteMovesLabel: displayed when its white to move
    private final JLabel whiteMovesLabel = new JLabel();
    // blackMovesLabel: displayed when its black to move
    private final JLabel blackMovesLabel = new JLabel();

    // optionList: contains buttons of options ComboBox
    private final String[] optionList = new String[]
            {"OPTIONS", "RESTART", "LOAD POSITION", "GET POSITION", "SETTINGS"};
    private final JComboBox<String> options = new JComboBox<>(optionList);
    // settingsPanel: settings panel used to change properties of CheckersApp
    private JComponent[] settingsPanel;
    private JComboBox<String> gameModeOptions;
    private JComboBox<String> boardColorOptions;

    // COLUMN_SIZE: column size of the SidePanel
    public static final int COLUMN_SIZE = 4;
    // moveLabelBorderColor: custom border color for moveLabels
    private static final Color moveLabelBorderColor = new Color(56, 199, 28);
    // moveLabelBorderThickness: custom border thickness for moveLabels
    private static final int moveLabelBorderThickness = 3;

    /**
     * Constructs the SidePanel with given custom settings
     */
    public SidePanel() {
        panel.setPreferredSize(new Dimension(CheckersApp.BOARD_SIZE * 100,
                (int) (CheckersApp.SIDE_PANEL_HEIGHT * 100)));
        panel.setLayout(new GridLayout(1, COLUMN_SIZE));

        setUpOptions(options); setUpMoveLabels(); setUpSettings();

        panel.add(evaluation, Component.CENTER_ALIGNMENT);
        panel.add(whiteMovesLabel, Component.CENTER_ALIGNMENT);
        panel.add(blackMovesLabel, Component.CENTER_ALIGNMENT);
        panel.add(options, Component.CENTER_ALIGNMENT);
    }

    /**
     * Sets up the options JComboBox
     */
    private void setUpOptions(JComboBox<String> options) {
        options.setBackground(Color.DARK_GRAY);
        options.setForeground(Color.WHITE);
        options.addActionListener(this);

        // center-aligned items
        DefaultListCellRenderer listRenderer = new DefaultListCellRenderer();
        listRenderer.setHorizontalAlignment(DefaultListCellRenderer.CENTER);
        options.setRenderer(listRenderer);
    }
    /**
     * Sets up the moveLabels
     */
    private void setUpMoveLabels() {
        evaluation.setOpaque(true); whiteMovesLabel.setOpaque(true); blackMovesLabel.setOpaque(true);
        evaluation.setForeground(Color.WHITE); evaluation.setBackground(Color.DARK_GRAY);
        whiteMovesLabel.setBackground(Color.WHITE); blackMovesLabel.setBackground(Color.BLACK);

        evaluation.setFont(new Font("Arial", Font.PLAIN, 40));
        whiteMovesLabel.setBorder(BorderFactory.createLineBorder
                (moveLabelBorderColor, moveLabelBorderThickness));
        blackMovesLabel.setBorder(whiteMovesLabel.getBorder()); blackMovesLabel.setVisible(false);
    }
    /**
     * Sets up the settings JComponent[]
     */
    private void setUpSettings() {
        String[] gameModeOptionList = new String[] {"2 PLAYER MODE", "PLAYER VS BOT", "2 BOT MODE", "PLAYER VS CENG"};
        gameModeOptions = new JComboBox<>(gameModeOptionList); setUpOptions(gameModeOptions);

        String[] tileColorOptionList = new String[] {"BLACK", "GREEN", "WOOD"};
        boardColorOptions = new JComboBox<>(tileColorOptionList); setUpOptions(boardColorOptions);

        settingsPanel = new JComponent[] {
                new JLabel("Game Mode"), gameModeOptions,
                new JLabel("Board Color"), boardColorOptions
        };
    }

    /**
     * Updates moveLabels based on current game position
     * @param move side to move (white / black)
     */
    public void updateMoveLabels(boolean move) {
        evaluation.setText(String.valueOf(Board.getPositionEvaluation()));
        whiteMovesLabel.setVisible(move);
        blackMovesLabel.setVisible(!move);
    }

    /**
     * Handles user input related to LOAD POSITION
     */
    private static void loadPositions() {
        String inputPDN = JOptionPane.showInputDialog(null,
                "Enter custom position PDN", null);

        if (inputPDN != null && PDN.isAppropriate(inputPDN))
            CheckersApp.restartGame(inputPDN);
        else if (inputPDN != null)
            JOptionPane.showMessageDialog(null, inputPDN
                    + " is not recognized PDN for Board of "
                    + CheckersApp.BOARD_SIZE + "X" + CheckersApp.BOARD_SIZE);
    }
    /**
     * Retrieves current position in form of PDN, displaying it in a popup
     */
    private static void getPosition() {
        JOptionPane.showMessageDialog(null, "PDN for current" +
                " Board position is:\n " + PDN.getBoardPosition());
    }

    /**
     * Handles user input in settings panel (JComponent[])
     */
    private void getSettings() {
        int result = JOptionPane.showConfirmDialog
                (null, settingsPanel, "Settings", JOptionPane.DEFAULT_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String selectedGameMode = (String) gameModeOptions.getSelectedItem();
            switch (gameModeOptions.getSelectedIndex()) { // game mode options
                case 0, 1 -> CheckersApp.GAME_MODE = selectedGameMode;
                case 2 -> { CheckersApp.GAME_MODE = selectedGameMode; RandomBot.run(); }
                case 3 -> CheckersApp.GAME_MODE = selectedGameMode;
            }
            switch (boardColorOptions.getSelectedIndex()) { // board color options
                case 0 -> CheckersApp.TILE_TYPE = GameDesign.defaultTile;
                case 1 -> CheckersApp.TILE_TYPE = GameDesign.greenComboTile;
                case 2 -> CheckersApp.TILE_TYPE = GameDesign.woodComboTile;
            } Board.rePaint();
        }
    }

    /**
     * Checks for and accommodates selected buttons / requests in SidePanel
     * @param e selected button
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == options && options.getSelectedItem() instanceof String) {
            String option = (String) options.getSelectedItem();
            switch (option) {
                case "RESTART" -> CheckersApp.restartGame(CheckersApp.STARTING_PDN);
                case "LOAD POSITION" -> loadPositions();
                case "GET POSITION" -> getPosition();
                case "SETTINGS" -> getSettings();
            } options.setSelectedIndex(0);
        }
    }
}
