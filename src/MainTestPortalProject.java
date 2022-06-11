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
//Oumar's Workspace Ending

//Humayun's Workspace Starting
//Humaayun's Workspace Ending

//Main Workspace(Collective)
    public static void main(String[] args) {


    }
}
