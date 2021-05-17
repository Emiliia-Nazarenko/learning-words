import db.ArchiveEntity;
import db.WordEntity;
import db.WordsEntity;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.regex.Pattern;

public class Form {

    private JPanel panel1;
    private JTextField fieldWord;
    private JTextField fieldTranslation;
    private JLabel label;
    private JButton startButton;
    private JButton addButton;
    private JButton deleteButton;
    private JButton showWordsButton;
    private JButton showWordsInArchiveButton;

    UserDAO user = new UserDAO();
    WordsEntity w = new WordsEntity();
    List<WordEntity> list = user.getRows(w);
    private final int attempts = 5;
    WordEntity currentW;
    int currentIndex;

    private void wrongAnswerMessage(){
        label.setText("Answer is wrong!");
        label.setForeground(Color.RED);
    }

    private void rightAnswerMessage(){
        label.setText("Answer is right!");
        label.setForeground(Color.GREEN);
    }

    public Form() {

        addButton.addActionListener(e -> {
            WordsEntity word;
            try {
                if(Pattern.matches("\s+", fieldWord.getText()) ||
                        Pattern.matches("\s+", fieldTranslation.getText()) ||
                        fieldWord.getText().equals("") || fieldTranslation.getText().equals(""))
                    throw new Exception();
                word = new WordsEntity(fieldWord.getText(), fieldTranslation.getText());
                user.save(word);
                list = user.getRows(w);
                label.setText("Adding was successful!");
                label.setForeground(Color.GREEN);
                fieldWord.setText(null);
                fieldTranslation.setText(null);
            } catch (Exception exception) {
                label.setText("Adding was not successful!");
                label.setForeground(Color.RED);
                JOptionPane.showMessageDialog(panel1, "Text field is empty.");
            }
        });

        deleteButton.addActionListener(e -> {
            try {
                String word = fieldWord.getText();
                if(Pattern.matches("\s+", word) ||
                        word.equals("") || !user.isInDB(word))
                    throw new Exception();
                user.delete(fieldWord.getText());
                label.setText("Deletion was successful!");
                label.setForeground(Color.GREEN);
                list = user.getRows(w);
                fieldWord.setText(null);
                fieldTranslation.setText(null);
            } catch (Exception exception) {
                label.setText("Deletion was not successful!");
                label.setForeground(Color.RED);
                JOptionPane.showMessageDialog(panel1, "This word does not exist.");
            }
        });

        startButton.addActionListener(e -> {
            try {
                if(list.size() == 0)
                    JOptionPane.showMessageDialog(panel1, "You know everything!");
                if(startButton.getText().equals("Start")) {
                    for(WordEntity item : list) {
                        if(item.getAttempts_w() < attempts) {
                            fieldWord.setText(item.getWord());
                            startButton.setText("Ok");
                            currentW = item;
                            currentIndex = list.indexOf(item);
                            break;
                        } else if(item.getAttempts_t() < attempts) {
                            fieldWord.setText(item.getTranslation());
                            startButton.setText("Ok");
                            currentW = item;
                            currentIndex = list.indexOf(item);
                            break;
                        } else {
                            user.save(new ArchiveEntity(item));
                            user.delete(item.getWord());
                        }
                    }
                    list = user.getRows(w);
                } else {
                    if(currentW.getWord().equals(fieldWord.getText())) {
                        if(currentW.getTranslation().equals(fieldTranslation.getText())) {
                            rightAnswerMessage();
                            user.increaseAttempts_w(currentW);
                        } else {
                            wrongAnswerMessage();
                            user.decreaseAttempts_w(currentW);
                        }
                    } else if(currentW.getTranslation().equals(fieldWord.getText())) {
                        if(currentW.getWord().equals(fieldTranslation.getText())) {
                            rightAnswerMessage();
                            user.increaseAttempts_t(currentW);
                        } else {
                            wrongAnswerMessage();
                            user.decreaseAttempts_t(currentW);
                        }
                    }
                    if(currentW.getAttempts_t() == attempts && currentW.getAttempts_w() == attempts) {
                        user.save(new ArchiveEntity(currentW));
                        user.delete(currentW.getWord());
                        list = user.getRows(w);
                    }
                    if(list.size() == 0)
                        JOptionPane.showMessageDialog(panel1, "You know everything!");
                    else {
                        if(currentIndex >= list.size() - 1) {
                            currentW = list.get(0);
                            currentIndex = 0;
                        } else {
                            currentIndex++;
                            currentW = list.get(currentIndex);
                        }
                        if(currentW.getAttempts_w() < attempts) {
                            fieldWord.setText(currentW.getWord());
                        } else if(currentW.getAttempts_t() < attempts) {
                            fieldWord.setText(currentW.getTranslation());
                        } else {
                            user.save(new ArchiveEntity(currentW));
                            user.delete(currentW.getWord());
                            for(WordEntity item : list) {
                                if(item.getAttempts_w() < attempts) {
                                    fieldWord.setText(item.getWord());
                                    currentW = item;
                                    currentIndex = list.indexOf(item);
                                    break;
                                } else if(item.getAttempts_t() < attempts) {
                                    fieldWord.setText(item.getTranslation());
                                    currentW = item;
                                    currentIndex = list.indexOf(item);
                                    break;
                                } else {
                                    user.save(new ArchiveEntity(item));
                                    user.delete(item.getWord());
                                }
                            }
                        }
                    }
                    fieldTranslation.setText("");
                }
                fieldTranslation.requestFocus();
                startButton.requestFocus(true);
                fieldTranslation.requestFocus(true);
            } catch (HeadlessException headlessException) {
                JOptionPane.showMessageDialog(panel1, "Something went wrong!=(");
            }
        });

        showWordsButton.addActionListener(e -> {
            WordsEntity wordsEntity = new WordsEntity();
            user.showTable(wordsEntity);
        });

        showWordsInArchiveButton.addActionListener(e -> {
            ArchiveEntity archiveEntity = new ArchiveEntity();
            user.showTable(archiveEntity);
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Learning words");
        frame.setContentPane(new Form().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
