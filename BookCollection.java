package com.company;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

public class BookCollection {
    private static DefaultTableModel tableModel;
    private static final String[] columnNames = {"ID", "TITLE", "AUTHOR", "PUBLISHING", "YEAR", "COUNT"};
    private static boolean disable = false;
    private static boolean correct = false;

    public static void GUI() {
        tableModel = new DefaultTableModel(columnNames, 0);

        JFrame frame = new JFrame("Library");
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton search = new JButton("SEARCH");
        JButton add = new JButton("ADD");
        JButton giveBack = new JButton("Give Back");
        JButton refresh = new JButton("Refresh");
        JTable bookTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(bookTable);

        bookTable.setBorder(BorderFactory.createBevelBorder(5, Color.red, Color.blue));
        scrollPane.setBorder(BorderFactory.createBevelBorder(5, Color.red, Color.blue));
        scrollPane.setBackground(new Color(94, 93, 93));
        scrollPane.setPreferredSize(new Dimension(700, 400));
        bookTable.setIntercellSpacing(new Dimension(10, 0));

        search.setBounds(50, 50, 100, 50);
        frame.add(search);
        add.setBounds(50, 120, 100, 50);
        frame.add(add);
        giveBack.setBounds(50, 190, 100, 50);
        frame.add(giveBack);
        refresh.setBounds(50, 260, 100, 50);
        frame.add(refresh);
        panel.setBackground(Color.gray);
        frame.add(panel);
        frame.setSize(1000,500);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 40));
        panel.add(scrollPane);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add.addActionListener(e -> {
            if (!disable) {
                disable = true;
                addBook();
            }
        });
        search.addActionListener(e -> {
            if (!disable) {
                disable = true;
                findBook();
            }
        });
        refresh.addActionListener(e -> {
            if (!disable) {
                disable = true;
                addBooksFromFile();
            }
        });
        giveBack.addActionListener(e -> {
            if (!disable) {
                disable = true;
                giveBackBook();
            }
        });
        bookTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (!disable) {
                    disable = true;
                    int row = bookTable.rowAtPoint(evt.getPoint());
                    if (row >= 0 && Integer.parseInt((String) tableModel.getValueAt(row, 5)) > 1) {
                        borrowBookGUI(row, (String) tableModel.getValueAt(row, 1), (String) tableModel.getValueAt(row, 2), (String) tableModel.getValueAt(row, 3));
                    } else {
                        borrowBookGUI(row, (String) tableModel.getValueAt(row, 1), (String) tableModel.getValueAt(row, 2), (String) tableModel.getValueAt(row, 3));
                    }
                }
            }
        });
        addBooksFromFile();
    }

    public static void addBooksFromFile() {
        tableModel.setRowCount(0);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File("BookBase.xml"));
            doc.getDocumentElement().normalize();
            int idBook = 1;
            NodeList list = doc.getElementsByTagName("Book");
            for (int temp = 0; temp < list.getLength(); temp++) {
                Node node = list.item(temp);
                Element element = (Element) node;

                String title = element.getElementsByTagName("Title").item(0).getTextContent();
                String author = element.getElementsByTagName("Author").item(0).getTextContent();
                String publishing = element.getElementsByTagName("Publishing").item(0).getTextContent();
                String year = element.getElementsByTagName("Year").item(0).getTextContent();
                String count = element.getElementsByTagName("Count").item(0).getTextContent();
                if (!count.equals("0")) {
                    tableModel.addRow(new Object[]{idBook, title, author, publishing, year, count});
                    idBook++;
                }
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        disable = false;
    }

    public static void addBook() {
        JFrame frame = new JFrame("Add Book");
        JPanel panel = new JPanel();
        JButton apply = new JButton("Apply");
        JButton cancel = new JButton("Cancel");
        JLabel title = new JLabel("Title");
        JLabel author = new JLabel("Author");
        JLabel publishing = new JLabel("Publishing");
        JLabel year = new JLabel("Year");
        JLabel count = new JLabel("Count");
        JTextField fieldTitle = new JTextField();
        JTextField fieldAuthor = new JTextField();
        JTextField fieldPublishing = new JTextField();
        JTextField fieldYear = new JTextField();
        JTextField fieldCount = new JTextField();

        panel.setSize(310, 600);
        panel.setBackground(Color.darkGray);
        panel.setLayout(null);
        frame.setSize(310, 600);

        title.setBounds(130, 10, 100, 30);
        title.setFont(new Font(title.getFont().getName(), Font.PLAIN, 20));
        title.setForeground(Color.WHITE);
        fieldTitle.setBounds(50, 50, 200, 30);

        author.setBounds(120, 90, 100, 30);
        author.setFont(new Font(author.getFont().getName(), Font.PLAIN, 20));
        author.setForeground(Color.WHITE);
        fieldAuthor.setBounds(50, 130, 200, 30);

        publishing.setBounds(100, 170, 100, 30);
        publishing.setFont(new Font(publishing.getFont().getName(), Font.PLAIN, 20));
        publishing.setForeground(Color.WHITE);
        fieldPublishing.setBounds(50, 210, 200, 30);

        year.setBounds(130, 250, 100, 30);
        year.setFont(new Font(year.getFont().getName(), Font.PLAIN, 20));
        year.setForeground(Color.WHITE);
        fieldYear.setBounds(50, 290, 200, 30);

        count.setBounds(120, 330, 100, 30);
        count.setFont(new Font(count.getFont().getName(), Font.PLAIN, 20));
        count.setForeground(Color.WHITE);
        fieldCount.setBounds(50, 370, 200, 30);

        apply.setBounds(50, 450, 90, 50);
        cancel.setBounds(160, 450, 90, 50);

        frame.add(panel);
        panel.add(title);
        panel.add(fieldTitle);
        panel.add(author);
        panel.add(fieldAuthor);
        panel.add(publishing);
        panel.add(fieldPublishing);
        panel.add(year);
        panel.add(fieldYear);
        panel.add(count);
        panel.add(fieldCount);
        panel.add(apply);
        panel.add(cancel);

        cancel.addActionListener(e -> {
            frame.setVisible(false);
            disable = false;
        });
        apply.addActionListener(e -> {
            boolean exists = false;
            int position = 0;
            for (int i = 0; i < tableModel.getRowCount(); i++) {

                if (tableModel.getValueAt(i, 1).equals(fieldTitle.getText()) && tableModel.getValueAt(i, 2).equals(fieldAuthor.getText())) {
                    exists = true;
                    position = i;
                }
            }
            if (exists) {
                updateFile(fieldAuthor.getText(), fieldPublishing.getText(), fieldYear.getText(), fieldCount.getText(), "0");
                int sum = Integer.parseInt(tableModel.getValueAt(position, 5).toString()) + Integer.parseInt(fieldCount.getText());
                tableModel.setValueAt(String.valueOf(sum), position, 5);

            } else {
                try {
                    if (fieldTitle.getText().isEmpty() & !fieldAuthor.getText().isEmpty() && !fieldPublishing.getText().isEmpty() && !fieldYear.getText().isEmpty() && !fieldCount.getText().isEmpty()) {
                        updateFile(fieldTitle.getText(), fieldAuthor.getText(), fieldPublishing.getText(), fieldYear.getText(), fieldCount.getText());
                        tableModel.addRow(new Object[]{tableModel.getRowCount() + 1, fieldTitle.getText(), fieldAuthor.getText(), fieldPublishing.getText(), fieldYear.getText(), fieldCount.getText()});
                        frame.setVisible(false);
                        disable = false;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }

    public static void findBook() {
        JFrame frame = new JFrame("Find a book");
        JPanel panel = new JPanel();
        JButton apply = new JButton("Apply");
        JButton cancel = new JButton("Cancel");
        JTextField text = new JTextField();
        JLabel search = new JLabel("What are you looking for?");

        panel.setSize(300, 200);
        panel.setBackground(Color.darkGray);
        panel.setLayout(null);
        frame.setSize(300, 200);

        search.setBounds(30, 10, 280, 30);
        search.setFont(new Font(search.getFont().getName(), Font.PLAIN, 20));
        search.setForeground(Color.WHITE);
        text.setBounds(40, 50, 200, 30);
        apply.setBounds(40, 100, 90, 30);
        cancel.setBounds(150, 100, 90, 30);

        frame.add(panel);
        panel.add(text);
        panel.add(apply);
        panel.add(cancel);
        panel.add(search);

        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cancel.addActionListener(e -> {
            frame.setVisible(false);
            disable = false;
        });
        apply.addActionListener(e -> {
            String what = text.getText().toLowerCase(Locale.ROOT);
            DefaultTableModel table = new DefaultTableModel(columnNames, 0);
            boolean objFound = false;
            int count = 1;
            for (int i = 0; i < tableModel.getRowCount(); ++i) {
                String firstEl = String.valueOf(tableModel.getValueAt(i, 1)).toLowerCase(Locale.ROOT);
                String secondEl = String.valueOf(tableModel.getValueAt(i, 2)).toLowerCase(Locale.ROOT);
                String thirdEl = String.valueOf(tableModel.getValueAt(i, 3)).toLowerCase(Locale.ROOT);
                String fourthEl = String.valueOf(tableModel.getValueAt(i, 4)).toLowerCase(Locale.ROOT);
                System.out.println("what: " + what);
                System.out.println("firstEl: " + firstEl);
                System.out.println("secondEl: " + secondEl);
                System.out.println("thirdEl: " + thirdEl);
                System.out.println("fourthEl: " + fourthEl);
                if (firstEl.contains(what) || secondEl.contains(what) || thirdEl.contains(what) || fourthEl.contains(what)) {
                    table.addRow(new Object[]{count, tableModel.getValueAt(i, 1), tableModel.getValueAt(i, 2), tableModel.getValueAt(i, 3), tableModel.getValueAt(i, 4), tableModel.getValueAt(i, 5)});
                    objFound = true;
                    System.out.println("Istnieje : " + i);
                }
                System.out.println("---------------------------------------");
            }

            if (objFound) {
                tableModel.setRowCount(0);
                for (int i = 0; i < table.getRowCount(); ++i) {
                    tableModel.addRow(new Object[]{count, table.getValueAt(i, 1), table.getValueAt(i, 2), table.getValueAt(i, 3), table.getValueAt(i, 4), table.getValueAt(i, 5)});
                    count++;
                }
            }
            frame.setVisible(false);
            disable = false;
        });
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }

    public static void setId() {
        for (int i = 0; i < tableModel.getRowCount(); ++i) {
            tableModel.setValueAt(i + 1, i, 0);
        }
    }

    public static void userFile(String firstName, String secondName, String id, String bookTitle, String bookAuthor, String bookPublishing) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse("UserNames.xml");

            Node employee = doc.getElementsByTagName("UserBase").item(0);

            Element book = doc.createElement("User");
            employee.appendChild(book);

            Element newFirstName = doc.createElement("FirstName");
            newFirstName.appendChild(doc.createTextNode(firstName));
            book.appendChild(newFirstName);

            Element newSecondName = doc.createElement("SecondName");
            newSecondName.appendChild(doc.createTextNode(secondName));
            book.appendChild(newSecondName);

            Element newId = doc.createElement("ID");
            newId.appendChild(doc.createTextNode(id));
            book.appendChild(newId);

            Element newTitle = doc.createElement("Title");
            newTitle.appendChild(doc.createTextNode(bookTitle));
            book.appendChild(newTitle);

            Element newAuthor = doc.createElement("Author");
            newAuthor.appendChild(doc.createTextNode(bookAuthor));
            book.appendChild(newAuthor);

            Element newPublishing = doc.createElement("Publishing");
            newPublishing.appendChild(doc.createTextNode(bookPublishing));
            book.appendChild(newPublishing);

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            DOMSource src = new DOMSource(doc);
            StreamResult res = new StreamResult(new File("UserNames.xml"));
            transformer.transform(src, res);
            doc.normalize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeData(String title, String author) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse("BookBase.xml");

            NodeList test = doc.getElementsByTagName("Book");

            for (int i = 0; i < test.getLength(); ++i) {
                NodeList newList = test.item(i).getChildNodes();
                if (newList.item(3).getTextContent().equals(title) && newList.item(5).getTextContent().equals(author)) {
                    int count = Integer.parseInt(newList.item(11).getTextContent()) - 1;
                    newList.item(11).setTextContent(String.valueOf(count));
                }
            }
            for (int i = 0; i < test.getLength(); ++i) {
                NodeList newList = test.item(i).getChildNodes();
                newList.item(1).setTextContent(String.valueOf(i + 1));
            }

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            DOMSource src = new DOMSource(doc);
            StreamResult res = new StreamResult(new File("BookBase.xml"));
            transformer.transform(src, res);
            doc.normalize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateFile(String title, String author, String publishing, String year, String count) {
        try {
            boolean exists = false;
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse("BookBase.xml");

            Node employee = doc.getElementsByTagName("BooksBase").item(0);
            NodeList test = doc.getElementsByTagName("Book");

            for (int i = 0; i < test.getLength(); ++i) {
                NodeList newList = test.item(i).getChildNodes();
                if (newList.item(3).getTextContent().equals(title) && newList.item(5).getTextContent().equals(author)) {
                    int bookCount = Integer.parseInt(newList.item(11).getTextContent()) + 1;
                    newList.item(11).setTextContent(String.valueOf(bookCount));
                    exists = true;
                }
            }
            if (!exists) {
                Element book = doc.createElement("Book");
                employee.appendChild(book);

                Element bookID = doc.createElement("ID");
                bookID.appendChild(doc.createTextNode(String.valueOf(test.getLength())));
                book.appendChild(bookID);

                Element bookTitle = doc.createElement("Title");
                bookTitle.appendChild(doc.createTextNode(String.valueOf(title)));
                book.appendChild(bookTitle);

                Element bookAuthor = doc.createElement("Author");
                bookAuthor.appendChild(doc.createTextNode(author));
                book.appendChild(bookAuthor);

                Element bookPublishing = doc.createElement("Publishing");
                bookPublishing.appendChild(doc.createTextNode(publishing));
                book.appendChild(bookPublishing);

                Element bookYear = doc.createElement("Year");
                bookYear.appendChild(doc.createTextNode(year));
                book.appendChild(bookYear);

                Element bookCount = doc.createElement("Count");
                bookCount.appendChild(doc.createTextNode(count));
                book.appendChild(bookCount);
            }
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            DOMSource src = new DOMSource(doc);
            StreamResult res = new StreamResult(new File("BookBase.xml"));
            transformer.transform(src, res);
            doc.normalize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void borrowBookGUI(int row, String bookTitle, String bookAuthor, String bookPublishing) {
        JFrame frame = new JFrame("Borrow Book");
        JPanel panelLeft = new JPanel();
        JPanel panelRight = new JPanel();
        JButton apply = new JButton("Apply");
        apply.setFont(new Font(apply.getFont().getName(), Font.PLAIN, 20));
        JButton cancel = new JButton("Cancel");
        cancel.setFont(new Font(cancel.getFont().getName(), Font.PLAIN, 20));
        apply.setBounds(80, 190, 100, 50);
        panelLeft.add(apply);
        cancel.setBounds(320, 190, 100, 50);
        panelLeft.setLayout(null);
        panelRight.setLayout(null);

        //USER
        JLabel firstName = new JLabel("First Name");
        firstName.setForeground(Color.WHITE);
        firstName.setFont(new Font(firstName.getFont().getName(), Font.PLAIN, 18));
        JTextField firstNameField = new JTextField();
        firstName.setBounds(20, 10, 200, 20);
        firstNameField.setBounds(20, 40, 200, 20);


        JLabel secondName = new JLabel("Second Name");
        secondName.setForeground(Color.WHITE);
        secondName.setFont(new Font(secondName.getFont().getName(), Font.PLAIN, 18));
        JTextField secondNameField = new JTextField();
        secondName.setBounds(20, 70, 200, 20);
        secondNameField.setBounds(20, 100, 200, 20);

        JLabel cardNumber = new JLabel("ID / Club card number");
        cardNumber.setForeground(Color.WHITE);
        cardNumber.setFont(new Font(cardNumber.getFont().getName(), Font.PLAIN, 18));
        JTextField cardNumberField = new JTextField();
        cardNumber.setBounds(20, 130, 200, 20);
        cardNumberField.setBounds(20, 160, 200, 20);

        //Book
        JLabel title = new JLabel("Title");
        title.setForeground(Color.WHITE);
        JLabel titleField = new JLabel(bookTitle);
        title.setBounds(260, 10, 200, 20);
        titleField.setBounds(260, 40, 200, 20);
        title.setFont(new Font(title.getFont().getName(), Font.PLAIN, 18));
        titleField.setFont(new Font(titleField.getFont().getName(), Font.PLAIN, 16));
        titleField.setBackground(Color.white);
        titleField.setOpaque(true);
        titleField.setBorder(new EmptyBorder(5, 10, 5, 10));

        JLabel author = new JLabel("Author");
        author.setForeground(Color.WHITE);
        JLabel authorField = new JLabel(bookAuthor);
        author.setBounds(260, 70, 200, 20);
        authorField.setBounds(260, 100, 200, 20);
        author.setFont(new Font(author.getFont().getName(), Font.PLAIN, 18));
        authorField.setFont(new Font(authorField.getFont().getName(), Font.PLAIN, 16));
        authorField.setBackground(Color.white);
        authorField.setOpaque(true);
        authorField.setBorder(new EmptyBorder(5, 10, 5, 10));

        JLabel publishing = new JLabel("Publishing");
        publishing.setForeground(Color.WHITE);
        JLabel publishingField = new JLabel(bookPublishing);
        publishing.setBounds(260, 130, 200, 20);
        publishingField.setBounds(260, 160, 200, 20);
        publishing.setFont(new Font(publishing.getFont().getName(), Font.PLAIN, 18));
        publishingField.setFont(new Font(publishingField.getFont().getName(), Font.PLAIN, 16));
        publishingField.setBackground(Color.white);
        publishingField.setOpaque(true);
        publishingField.setBorder(new EmptyBorder(5, 10, 5, 10));

        frame.add(panelLeft);
        frame.add(panelRight);
        frame.setSize(500, 300);
        panelLeft.setBounds(0, 0, 250, 300);
        panelRight.setBounds(0, 0, 250, 300);
        panelLeft.setBackground(Color.darkGray);
        panelRight.setBackground(Color.darkGray);

        panelLeft.add(apply);
        panelRight.add(cancel);
        panelLeft.add(firstName);
        panelLeft.add(firstNameField);
        panelLeft.add(secondName);
        panelLeft.add(secondNameField);
        panelLeft.add(cardNumber);
        panelLeft.add(cardNumberField);
        panelRight.add(title);
        panelRight.add(titleField);
        panelRight.add(author);
        panelRight.add(authorField);
        panelRight.add(publishing);
        panelRight.add(publishingField);

        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cancel.addActionListener(e -> {
            frame.setVisible(false);
            disable = false;
        });

        apply.addActionListener(e -> {
            String firstName1 = firstNameField.getText();
            String secondName1 = secondNameField.getText();
            String id = cardNumberField.getText();
            if (!Objects.equals(firstName1, "") && !Objects.equals(secondName1, "") && !Objects.equals(id, "")) {
                userFile(firstName1, secondName1, id, bookTitle, bookAuthor, bookPublishing);
                int count = Integer.parseInt((String) tableModel.getValueAt(row, 5));
                if (count > 1) {
                    tableModel.setValueAt(count - 1, row, 5);
                    removeData(titleField.getText(), authorField.getText());
                } else {
                    tableModel.removeRow(row);
                    removeData(titleField.getText(), authorField.getText());
                    setId();
                }
                frame.setVisible(false);
                disable = false;
            }
        });
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }

    public static void giveBackBook() {
        JFrame frame = new JFrame("Give Back");
        JPanel panel = new JPanel();
        JButton apply = new JButton("Apply");
        JButton cancel = new JButton("Cancel");
        apply.setBounds(40, 250, 80, 40);
        panel.add(apply);
        cancel.setBounds(160, 250, 80, 40);
        panel.setLayout(null);

        JLabel id = new JLabel("ID/Club Card Number");
        id.setForeground(Color.WHITE);
        id.setFont(new Font(id.getFont().getName(), Font.PLAIN, 18));
        JTextField idField = new JTextField();
        id.setBounds(50, 20, 200, 20);
        idField.setBounds(40,45, 200, 30);

        JLabel title = new JLabel("Title");
        title.setForeground(Color.WHITE);
        title.setFont(new Font(title.getFont().getName(), Font.PLAIN, 18));
        JTextField titleField = new JTextField();
        title.setBounds(120, 90, 200, 20);
        titleField.setBounds(40, 115, 200, 30);

        JLabel author = new JLabel("Author");
        author.setForeground(Color.WHITE);
        author.setFont(new Font(author.getFont().getName(), Font.PLAIN, 18));
        JTextField authorField = new JTextField();
        author.setBounds(110, 160, 200, 20);
        authorField.setBounds(40, 185, 200, 30);

        frame.add(panel);
        frame.setSize(300, 350);
        panel.setBounds(0, 0, 300, 350);
        panel.setBackground(Color.darkGray);


        panel.add(id);
        panel.add(idField);
        panel.add(title);
        panel.add(titleField);
        panel.add(author);
        panel.add(authorField);
        panel.add(apply);
        panel.add(cancel);

        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        cancel.addActionListener(e -> {
            frame.setVisible(false);
            disable = false;
        });
        apply.addActionListener(e -> {
            checkUserAndBook(idField.getText(), titleField.getText(), authorField.getText());
            frame.setVisible(!correct);
        });
    }

    public static void checkUserAndBook(String id, String title, String author) {
        try {
            DocumentBuilderFactory dbu = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbu.newDocumentBuilder();
            Document dbUser = db.parse("UserNames.xml");
            NodeList user = dbUser.getElementsByTagName("User");
            for (int i = 0; i < user.getLength(); ++i) {
                NodeList newList = user.item(i).getChildNodes();
                if (newList.item(5).getTextContent().equals(id) && newList.item(7).getTextContent().equals(title) && newList.item(9).getTextContent().equals(author)) {
                    updateFile(title, author, "a", "b", "c");
                    addBooksFromFile();
                    Element element = (Element) dbUser.getElementsByTagName("User").item(i);
                    element.getParentNode().removeChild(element);
                    correct = true;
                    disable = false;
                }
            }
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            DOMSource src = new DOMSource(dbUser);
            StreamResult res = new StreamResult(new File("UserNames.xml"));
            transformer.transform(src, res);
            dbUser.normalize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        GUI();
    }
}
