import java.util.Scanner;
import java.util.HashMap;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;


public class MainTestPortalProject {

    static Scanner scanner = new Scanner(System.in);//Global Scanner For Inputs Anywhere inside program

//Salman's Workspace
    public static void Quizz(){
        System.out.print("Choose any option : \ta : Students Section\tb : Teachers Section\tc : Administrative section");
        String choice = scanner.next();
        if("Aa".contains(choice)){
            StudentQuizSection();
        }else if("Bb".contains(choice)){
            TeacherQuizSection();
        }else if("Cc".contains(choice)){
            AdminQuizSection();
        }
    }

    private static void AdminQuizSection() {
    }

    private static void TeacherQuizSection() {
    }

    public static void StudentQuizSection(){

    }
//Salman's Workspace Ending

//Oumar's Workspace Starting
public class creation
{
    public static void main(String[] args) throws InvalidFormatException, IOException {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter a if you are a student, b if you are from admin.");
        String choiceF = input.next();
        String[] chalanFileNames;
        if (choiceF.equalsIgnoreCase("b")) {
            System.out.print("Enter \na: if you want to create a sample chalan..\nb: if you want to issue chalan forms to the students.");
            String choiceAdminF = input.next();
            if (choiceAdminF.equalsIgnoreCase("a")) {
                try {
                    XWPFDocument Document = new XWPFDocument();
                    FileOutputStream out = new FileOutputStream(new File("SAMPLECHALAN.docx"));
                    XWPFTable table = Document.createTable(2, 4);
                    Document.write(out);
                    out.close();
                    System.out.print("The file has been created");
                } catch (Exception e) {
                    System.out.println("e");
                }
            }
            else if (choiceAdminF.equalsIgnoreCase("b"))
            {
                System.out.print("Enter the degree program of students to whom you want to issue chalan forms");
                String studentDegreeProgram = input.next();
                String[][] studentsOfDegreeProgram = {{"BSE-058", "Muhammad Oumar"}, {"BSE-077", "Salman Tariq"}, {"BSE-082", "Muhammad Humayun"}}; /*The 2 statements can be replaced by mongoData for students in the departments*/
                int rows = 3;
                chalanFileNames = new String[rows];
                try {
                    System.out.println("Enter due date");
                    String dueDate = input.next();
                    System.out.println("Enter due fees");
                    String dueFees = input.next();
                    for (int b = 0; b < rows; b++) {
                        XWPFDocument Document1 = new XWPFDocument(OPCPackage.open("SAMPLECHALAN.docx"));
                        for (XWPFTable table : Document1.getTables()) {
                            for (XWPFTableRow row : table.getRows()) {
                                for (XWPFTableCell cell : row.getTableCells()) {
                                    for (XWPFParagraph p : cell.getParagraphs()) {
                                        for (XWPFRun r : p.getRuns()) {
                                            String text = r.getText(0);
                                            if (text != null && text.contains("STUDENT NAME:")) {
                                                text = text.replace("STUDENT NAME:", studentsOfDegreeProgram[b][1]);
                                                r.setText(text, 0);
                                            }
                                            if (text != null && text.contains("ROLL NUMBER:")) {
                                                text = text.replace("ROLL NUMBER:", studentsOfDegreeProgram[b][0]);
                                                r.setText(text, 0);
                                            }
                                            if (text != null && text.contains("PROGRAM:")) {
                                                text = text.replace("PROGRAM:", studentDegreeProgram);
                                                r.setText(text, 0);
                                            }
                                            if (text != null && text.contains("TOTAL FEE:")) {
                                                text = text.replace("TOTAL FEE:", dueFees);
                                                r.setText(text, 0);
                                            }
                                            if (text != null && text.contains("DUE DATE:")) {
                                                text = text.replace("DUE DATE:", dueDate);
                                                r.setText(text, 0);
                                            }
                                            /*An if for father names to be added using Mongo*/
                                        }
                                    }
                                }
                            }
                        }
                        for (int c = 0; c < 2; c++) {
                            if (c == 1) {
                                Document1.write(new FileOutputStream(studentsOfDegreeProgram[b][0].concat(studentsOfDegreeProgram[b][1]).concat("banker").concat(".docx")));
                                /*The file is uploaded on the banker's module*/
                            } else {
                                Document1.write(new FileOutputStream(studentsOfDegreeProgram[b][0].concat(studentsOfDegreeProgram[b][1]).concat(".docx")));
                                /*The file is uploaded on the student's module*/
                            }
                        }
                        chalanFileNames[b] = studentsOfDegreeProgram[b][0].concat(studentsOfDegreeProgram[b][1]).concat(".docx");
                    }
                    System.out.println("Chalan forms have been created for the students");
                } catch (IOException e) {
                    System.out.println("e");
                }
            }
        }
    }
    
    public static void QuizManagement() throws InterruptedException {
        System.out.println("Enter a if you are a student and b if you are a teacher.");
        Scanner input=new Scanner(System.in);
        String choiceQ=input.next();
        int totalStudentsInClass=5;
        String filename="any";
        String[] rollNumbers;
        String[] names;
        rollNumbers=new String[totalStudentsInClass];
        names=new String[totalStudentsInClass];
        int totalQuestions=15; /* can add input.nextInt()*/
        String[] answers;
        int[] qMarks=new int[totalStudentsInClass];
        double days1=0;
        double hours1=0;
        double minutes1=0;
        double MainStartTimeQuiz=1655050039699.0;
        double Deadline=0;
        if (choiceQ.equalsIgnoreCase("b")) {
            for (int k = 0; k < totalStudentsInClass; k++) {
                System.out.print("Enter the roll number of the student.");
                rollNumbers[k] = input.next();
                System.out.print("Enter the name of the student.");
                names[k] = input.next();
            }
            System.out.print("Enter \na: if you want to add a quiz and issue it to the students.\nb: if you want to check the quizes");
            String choiceTeacherQ = input.next();
            if (choiceTeacherQ.equalsIgnoreCase("a")) {
                answers = new String[totalQuestions];
                try {
                    XWPFDocument Document5 = new XWPFDocument();
                    FileOutputStream SampleQuiz = new FileOutputStream(new File("SampleQuiz2.docx"));
                    Document5.write(SampleQuiz);
                    SampleQuiz.close();
                    System.out.println("A file has been created of name \'SampleQuiz' with .docx extension.");
                    System.out.println("Please now make a quiz on this docx file.");
                    System.out.print("If you have made the quiz, enter a to make answer key.");
                    String inputing = input.next();
                    if (inputing.equalsIgnoreCase("a")) {
                        XWPFDocument Document6 = new XWPFDocument(OPCPackage.open("SampleQuiz2.docx"));
                        for (int o = 0; o <= totalQuestions; o++) {
                            System.out.println("Please enter the answer for question " + o);
                            String sa = input.next();
                            for (XWPFParagraph paragraph3 : Document6.getParagraphs()) {
                                for (XWPFRun r : paragraph3.getRuns()) {
                                    String text = r.getText(0);
                                    String concat1 = "Answer".concat(Integer.toString(o+1)).concat(":");
                                    if (text != null && text.contains(concat1)) {
                                        text = text.replace(concat1, concat1.concat(sa));
                                        r.setText(text, 0);
                                        answers[o] = concat1.concat(sa);
                                    }
                                }
                            }
                        }
                        Document6.write(new FileOutputStream("SampleQuizAnswerKey1.docx"));
                        System.out.println("Another file has been created as answer key of name \'SampleQuizAnswerKey' with .docx extension.");
                    }
                } catch (IOException | InvalidFormatException e) {
                    System.out.println("e");
                }
                for (int l = 0; l < totalStudentsInClass; l++) {
                    try {
                        filename = names[l].concat(rollNumbers[l]).concat(".docx");
                        XWPFDocument Document7 = new XWPFDocument(OPCPackage.open("SampleQuiz2.docx"));
                        Document7.write(new FileOutputStream(filename));
                        /*send this file to MongoDataBase to the respective student of this roll number*/
                    } catch (IOException | InvalidFormatException e) {
                        System.out.println("e");
                    }
                }
                System.out.println("Quizes have been uploaded.");
                System.out.println("Enter the time for submission in days,hours,minutes.");
                MainStartTimeQuiz=System.currentTimeMillis();
                double days=input.nextInt();
                days1=days*24*60*60000;
                double hours=input.nextInt();
                hours1=hours*60*60000;
                double minutes=input.nextInt();
                minutes1=minutes*60000;
                Deadline=days1+hours1+minutes1;
                if (System.currentTimeMillis()-MainStartTimeQuiz==Deadline)
                {
                    try {
                        for (int m = 0; m < totalStudentsInClass; m++) {
                            int sMarks = 0;
                            XWPFDocument docx = new XWPFDocument(new FileInputStream(rollNumbers[m].concat(names[m]).concat("1.docx")));
                            XWPFWordExtractor wordFile = new XWPFWordExtractor(docx);
                            String ultimate = wordFile.getText();
                            for (int n = 0; n < totalQuestions; n++) {
                                if (ultimate.contains(answers[n])) {
                                    sMarks = sMarks + 1;
                                }
                            }
                            qMarks[m] = sMarks;
                            System.out.println(sMarks);
                        }
                    } catch (IOException e) {
                        System.out.println("e");
                    }
                }
            }
        }
        if (choiceQ.equalsIgnoreCase("a"))
        {
            System.out.print("Enter your roll number please");
            String rNumber=input.next();
            System.out.print("Enter your name please");
            String studentName=input.next();
            if (System.currentTimeMillis()-MainStartTimeQuiz<Deadline)
            {
                try {
                    XWPFDocument Document9 = new XWPFDocument(OPCPackage.open(studentName.concat(rNumber).concat(".docx")));
                    double starttime = System.currentTimeMillis();
                    if (System.currentTimeMillis()-starttime<900000)
                    {
                        for (int o = 1; o <= totalQuestions; o++) {
                            if (System.currentTimeMillis()-starttime<900000)
                            {
                                System.out.println("Please enter the answer for question " + o);
                                if (System.currentTimeMillis()-starttime<900000)
                                {
                                    String studentAnswer = input.next();
                                    if (System.currentTimeMillis()-starttime<900000)
                                    {
                                        for (XWPFParagraph paragraph3 : Document9.getParagraphs()) {
                                            for (XWPFRun r : paragraph3.getRuns()) {
                                                String text = r.getText(0);
                                                String concat1 = "Answer".concat(Integer.toString(o)).concat(":");
                                                if (text != null && text.contains(concat1)) {
                                                    text = text.replace(concat1, concat1.concat(studentAnswer));
                                                    r.setText(text, 0);
                                                }
                                            }
                                        }
                                    }
                                    else
                                    {
                                        System.out.println("Time is over");
                                        break;
                                    }
                                }
                                else
                                {
                                    System.out.println("Time is over");
                                    break;
                                }
                            }
                            else
                            {
                                System.out.println("Time is over");
                                break;
                            }
                        }
                        Document9.write(new FileOutputStream(rNumber.concat(studentName).concat("1.docx")));
                    }
                }catch (IOException | InvalidFormatException e)
                {
                    System.out.print("e");
                }
            }
            else
            {
                System.out.println("Sorry! You are late than the given deadline");
            }
        }
    }
//Oumar's Workspace Ending

//Humayun's Workspace Starting
//Humaayun's Workspace Ending

//Main Workspace(Collective)
    public static void main(String[] args) {


    }
}
