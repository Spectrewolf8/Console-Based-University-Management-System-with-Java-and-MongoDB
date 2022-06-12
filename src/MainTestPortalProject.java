//imports
import java.util.*;
import java.util.logging.Logger; //To Disable MongoDB driver logs
import java.util.logging.Level; //To Disable MongoDB driver logs

//Oumar's imported libraries
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


// for mongodb
import com.mongodb.client.*;
import com.mongodb.MongoCredential;
import com.mongodb.client.model.Filters;

//for Bson Document and filters
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.JsonMode;
import org.bson.json.JsonWriterSettings;
import static com.mongodb.client.model.Filters.*;
import static java.util.Arrays.asList;

//for Json Document extractions;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.security.auth.Subject;

//start of main class
public class MainTestPortalProject {

    //global declarations

    static Scanner scanner = new Scanner(System.in); //Public Scanner
    static Scanner lineScanner = new Scanner(System.in);
    static Scanner IntScanner = new Scanner(System.in);
    static String choice; //made public to be used wherever needed
    // Creating a Public Mongo client Available
    public static MongoClient mongo = MongoClients.create("mongodb+srv://TestSemesterProjectContributor_01:TestSemesterProjectContributor_01@spectestcluster.gok1m.mongodb.net/?retryWrites=true&w=majority");
    // Accessing the database
    public static MongoDatabase MainDatabase = mongo.getDatabase("TestSemester2Project"); //making Database available globally to every module

    public static void DisableMongoLogs() {
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);
    }
    public static void ConnectDB() {

        // Creating Credentials
        MongoCredential credential = MongoCredential.createCredential("TestSemesterProjectContributor_01", "TestSemester2Project", "password".toCharArray());
        System.out.println("Connected to the database successfully");

        //Printing Credentials
        System.out.println("Credentials ::" + credential);
    }

    //Admin Section
    // Salman's workspace Starting
    //Just to initialize Collection for every module
    public static void initialize() {
        try {
            MainDatabase.createCollection("StudentsManagement");
        } catch (Exception exception) {
            System.err.println("Collection: StudentsManagement already Exists");
        }
        try {
            MainDatabase.createCollection("FeesManagement");
        } catch (Exception exception) {
            System.err.println("Collection: FeesManagement already Exists");
        }
        try {
            MainDatabase.createCollection("AttendanceManagement");
        } catch (Exception exception) {
            System.err.println("Collection: AttendanceManagement already Exists");
        }
        try {
            MainDatabase.createCollection("GradesManagement");
        } catch (Exception exception) {
            System.err.println("Collection: GradesManagement already Exists");
        }
        try {
            MainDatabase.createCollection("DegreesList");
        } catch (Exception exception) {
            System.err.println("Collection: Degrees already Exists");
        }
    }
    //choose, set and return BSON filters for searching
    public static Bson SearchFilters() {
        System.out.println("Which type of search do you want?\n\ta : Greater Than\n\tb : Less Than\n\tc : Exact value \n\td : Specific range");
        choice = scanner.next();
        if ("aA".contains(choice)) {
            System.out.print("Enter the Exact name of objects to filter(case sensitive) to greater tha" + "n or equal to filter : ");
            String object = scanner.next();
            System.out.print("Enter the Exact value for the object " + object + ":");
            String value = scanner.next();

            return gte(object, value);

        } else if ("bB".contains(choice)) {
            System.out.print("Enter the Exact name of objects to filter(case sensitive) to greater tha" + "n or equal to filter : ");
            String object = scanner.next();
            System.out.print("Enter the Exact value for the object " + object + ":");
            String value = scanner.next();

            return lte(object, value);
        } else if ("cC".contains(choice)) {
            System.out.print("Enter the Exact name of objects to filter(case sensitive) to greater tha" + "n or equal to filter : ");
            String object = scanner.next();
            System.out.print("Enter the Exact value for the object " + object + ":");
            String value = scanner.next();

            return eq(object, value);
        } else if ("dD".contains(choice)) {
            System.out.print("Enter the Exact name of objects to filter(case sensitive) to greater than or equal to filter : ");
            String object = scanner.next();
            System.out.print("Enter the Exact value for the object " + object + ":");
            String value = scanner.next();
            Bson ulfilter = gte(object, value);

            System.out.print("Enter the Exact name of objects to filter(case sensitive) to lesser than or equal to filter : ");
            object = scanner.next();
            System.out.print("Enter the Exact value for the object " + object + ":");
            value = scanner.next();
            Bson llfilter = lte(object, value);

            return and(ulfilter, llfilter);

        } else {
            System.out.println("invalid Filter");
        }

        return null;
    }
    //Degrees Public Hashmap
    //public static Map < String, String > DegreePrograms = new HashMap < String, String > ();


    //Pretty for formatting of Document being printed
    private static String pretty(Document document) {
        var settings = JsonWriterSettings.builder()
                .indent(true)
                .outputMode(JsonMode.SHELL)
                .build();
        return document.toJson(settings);
    }
    //function to call main menu everytime
    public static void Menu_main_Management() {

        ConnectDB(); // this function will connect our program to our Atlas Cluster
        DisableMongoLogs(); //this will disable Annoying mongoDB logs
        initialize();
        Degree_manager();
        StudentsManager();

    }
    ///**Modules**
    //public Fee collection access
    public static MongoCollection<Document> FeeCollection = MainDatabase.getCollection("FeesManagement");
    public static FindIterable<Document> FeeList = FeeCollection.find();
    //public Attendance collection access
    public static MongoCollection<Document> AttendanceCollection = MainDatabase.getCollection("AttendanceManagement");//opens Attendance management collection to work on
    public static FindIterable<Document> AttendanceList = AttendanceCollection.find();
    //public degree collection access
    public static MongoCollection < Document > DegreesCollection = MainDatabase.getCollection("DegreesList");
    public static FindIterable < Document > DegreesList = DegreesCollection.find();

    public static void AttendanceStudentLinker(String subjectName){
        Document NewSubjectToAddInAttendance = new Document("_id", subjectName);
        AttendanceCollection.insertOne(NewSubjectToAddInAttendance);
    }
    ///degree Management
    public static void Degree_manager() {

        //initializing the document again with filters
        System.out.print("Choose any action :\n\ta : view Degrees list\n\tb : manage Degrees");
        choice = scanner.next();
        if ("aA".contains(choice)) {
            System.out.println("---Degrees---");
            for (Document i: DegreesList) {
                System.out.println(pretty(i));
            }
        } else if ("bB".contains(choice)) {
            Degree_manager_ManageDegree();
        }

    }

    public static void Degree_manager_ManageDegree() {
        System.out.println("\n\ta : add a new Degree\n\tb : delete a degree\n\tx : cancel");
        choice = scanner.next();
        if ("aA".contains(choice)) {
            System.out.print("Enter courseID (one(TWO digits) word) : ");
            String courseID = scanner.next();
            System.out.print("Enter Course name without spaces in (AA-AA)format : ");
            String courseName = scanner.next();

            String[] Alphabets = new String[]{"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
            List<String> SectionsGen = new ArrayList<>();
            //String[] SubjectsList = new String[]{"",""};
            List<String> Subjects = new ArrayList<>();

            if ((courseID.length() == 2) && (courseName.length() == 5 && courseName.charAt(2) == '-')) {
                int NumOfCourses = 0;
                int NumOfSec = 0;

                try {
                    System.out.println("Enter the number of Sections to be made for this Course[max : 27] : ");
                    NumOfSec = IntScanner.nextInt();
                    System.out.print("Specify the number of Subjects to add in this Course : ");
                    NumOfCourses = IntScanner.nextInt();
                } catch (Exception e) {
                    System.out.println("Invalid input when Number(Integer) was required");
                    Degree_manager_ManageDegree();
                }

                for (int j = 1; j <= NumOfCourses; j++) {
                    System.out.println("Enter the name of Subject_" + j + " in the course(" + courseName + ")");
                    String SubjectName = lineScanner.next();
                    Subjects.add(SubjectName);
                    AttendanceStudentLinker(SubjectName);
                }

                for (int i = 0; i < NumOfSec; i++) {
                    SectionsGen.add(courseID + "-" + Alphabets[i]);
                }
                Document newDegree = new Document("_id", courseID)
                        .append("Course_Name", courseName)
                        .append("Sections", SectionsGen)
                        .append("SubjectsInThisCourse", Subjects);

                DegreesCollection.insertOne(newDegree);//inserting the created Degree
                System.out.print("Course with " + courseID + " and name " + courseName + " added");

            } else {
                System.err.println("Invalid courseID or Course name [returning]");
                Degree_manager();
            }

        } else if ("bB".contains(choice)) {
            System.out.println("---Degrees---");
            for (Document i: DegreesList) {
                System.out.println(pretty(i));
            }
            System.out.print("Enter the uniqueID of the course to delete : ");
            String DelCourseKey = scanner.next();
            DegreesCollection.deleteOne(Filters.eq("_id", DelCourseKey));
            System.out.println("Course with the key " + DelCourseKey);
        } else if ("xX".contains(choice)) {
            System.out.println("Cancelling...");
            Degree_manager();

        } else {
            System.out.println("Invalid Selection");
            Degree_manager_ManageDegree();
        }

    }

    //students Management Admin portion

    public static MongoCollection < Document > StudentCollection = MainDatabase.getCollection("StudentsManagement"); //open the Student  management collection to work on
    public static FindIterable < Document > StudentsList = StudentCollection.find(); //converts all documents in the Collection to iterable

    //main student manager module
    public static void StudentsManager() {

        System.out.println("Choose any management operation :\n\t a : View Students\n\t b : Manage Students\n\t e : EXIT");
        String choice = scanner.next();

        if ("bB".indexOf(choice) == 0 || "bB".indexOf(choice) == 1) { //checked for B like this because of the bug
            StudentsManager_Manage();
        } else if ("Aa".contains(choice)) {
            StudentManger_ViewStudentLis();
            System.out.println("Choose your next action :\n\ta : Return to Students Manager\n\tb : Return to main menu");
            choice = scanner.next();
            if ("aA".contains(choice)) {
                StudentsManager();
            } else if ("bB".contains(choice)) {
                Menu_main_Management();
            }
        } else {
            Menu_main_Management();

        }


    }
    //student manager submodules
    public static void StudentManger_ViewStudentLis() {
        System.out.println("Choose an option?\t\na : view all students \n\tb : Custom search with Filters\n\t press Any other key to cancel");
        choice = scanner.next();
        if ("aA".contains(choice)) {
            for (Document i: StudentsList) {
                System.out.println(pretty(i));
            }

        } else if ("bB".contains(choice)) {

            StudentsList = StudentCollection.find().filter(SearchFilters()); //initializing the document again with filters
            for (Document i: StudentsList) {
                System.out.println(pretty(i));
            }
        }

    }
    public static void StudentsManager_UpdateStudent() {
        System.out.print("Enter the exact uniqueID of the object to select update : ");
        String uniqueIDForUpdate = scanner.next();
        for (Document i: StudentCollection.find().filter(eq("_id", uniqueIDForUpdate))) {
            System.out.print("Selected object : \n\t" + pretty(i));
        }
        System.out.print("Enter the exact name of field to update : ");
        String ObjectName = scanner.next();
        System.out.print("Enter the exact value you want to update : ");
        String ObjectVal = lineScanner.nextLine();
        System.out.print("Enter the new value to update \"" + ObjectVal + "\" to : ");
        String ObjectValUpdate = scanner.next();
        choice = null;
        do {
            StudentCollection.updateOne(Filters.eq(ObjectName, ObjectVal), Updates.set(ObjectName, ObjectValUpdate));
            System.out.print("Make more changes to current selected object with ID " + uniqueIDForUpdate + " ?\n\t y : yes \n\t Any other key to cancel");
            choice = scanner.next();
            for (Document i: StudentCollection.find().filter(eq("_id", uniqueIDForUpdate))) {
                System.out.print("Selected object : \n\t" + pretty(i));
            }
            System.out.print("Successfully updated!!");
        } while ("yY".contains(choice));

    }

    public static void StudentsManager_Manage() {
        System.out.println("Choose any option :\n\ta : add students\n\tb : update students\n\tc : delete students\n\tx : Cancel");
        choice = scanner.next();

        if ("aA".contains(choice)) {
            StudentsManager_Manage_Add();

        } else if ("bB".contains(choice)) {
            StudentsManager_Manage_Update();

        } else if ("cC".contains(choice)) {
         StudentsManager_Manage_Delete();

        } else if ("xX".contains(choice)) {
            System.out.println("Cancelling");
            StudentsManager();

        } else {
            System.err.println("Invalid Selection");
            StudentsManager_Manage();

        }

    }

    public static void StudentsManager_Manage_Add() {
        System.out.print("Enter the registration number of the student in (AAA-AAA) format : ");
        String RegNum = lineScanner.nextLine();
        if (RegNum.charAt(3) == '-' && RegNum.length() == 7) {
            System.out.print("Enter Student's Name : ");
            String StudentName = lineScanner.nextLine();
            System.out.print("Enter Student's Father name : ");
            String StudentFatherName = lineScanner.nextLine();
            System.out.print("**Choose Department of the student : **");
            System.out.println("Following are the list of available Degrees, choose one to manage students in : ");
            System.out.println("---Degrees---");
            for (Document i: DegreesList) {
                System.out.println(pretty(i));
            }
            System.out.println("Enter the (exact)UniqueID of the Degree to select as current : ");
            String SelectDepartment = scanner.next();
            // List<Document> SelectedDegree = DegreesCollection.find().filter(eq("_id", SelectDegree)).into(new ArrayList<>());

            Document SelectedDegreeObjForDept = StudentManager_DepartmentRetrieve(SelectDepartment.toUpperCase());
            JSONObject SelectedDegreeObject = new JSONObject(SelectedDegreeObjForDept);
            String Extracted_Degree = SelectedDegreeObject.get("Course_Name").toString();

            Document SelectedDegreeObjForSectionsList = StudentManager_SectionsListRetrieve(SelectDepartment.toUpperCase());
            JSONObject SelectedDegreeObjForSectionsListJson = new JSONObject(SelectedDegreeObjForSectionsList);
            JSONArray SelectedDegreeSectionsObject = (JSONArray) SelectedDegreeObjForSectionsListJson.get("Sections");

            //for(int j = 0;j<SelectedDegreeSectionsObject.length();j++){
             //   System.out.println(List.of(SelectedDegreeSectionsObject).get(j));
            //}
            System.out.println("--Sections--");
            ArrayList<Object> SectionsSelectionBuffer = new ArrayList<>();
            int x = 1;
            for (Object i : SelectedDegreeSectionsObject){
                System.out.println(x + " : " + i);
                x++;
                SectionsSelectionBuffer.add(i);
            }
            System.out.println("please enter Section's index to choose for the student : ");
            boolean BadInput = true;
            int SecSerial;
            String SelectedSection = null;
            while(BadInput){
                try{
                    SecSerial= IntScanner.nextInt();
                    SelectedSection = (String) SectionsSelectionBuffer.get(SecSerial-1);
                    BadInput = false;
                }catch (InputMismatchException | IndexOutOfBoundsException e){
                    System.err.println("Invalid Input!! Please provide valid index : " );
                }
            }

           // System.out.println(SelectedDegreeSectionsObject);

            Document NewStudentData = new Document("_id", RegNum)
                    .append("StudentName", StudentName)
                    .append("FatherName", StudentFatherName)
                    .append("Department", SelectDepartment)
                    .append("Course_Name", Extracted_Degree);

            Document StudentDataForAttendance = new Document("_id", RegNum)
                    .append("Student Name", StudentName)
                    .append("Class",SelectedSection)
                    .append("Total Presents", 0)
                    .append("Total Absents", 0);

            Document StudentDataForFeeManagement = new Document("_id", RegNum)
                    .append("StudentName", StudentName)
                    .append("FatherName", StudentFatherName)
                    .append("Course_Name", Extracted_Degree);
            //Creating records(Document) for each new added student in other collections
            FeeCollection.insertOne(StudentDataForFeeManagement);
            AttendanceCollection.insertOne(StudentDataForAttendance);
            StudentCollection.insertOne(NewStudentData);
            //prompting user
            System.out.println("Student Created : " + StudentName + " - " + RegNum);
            System.out.println("Add more students?\n\ta : yes\n\tAny other key to exit");
            choice = scanner.next();
            if ("aA".contains(choice)){
                StudentsManager_Manage_Add();
            }else{
                System.out.println("Exiting...");
            }
        } else {
            System.err.println("Invalid RegNum" + RegNum);
            StudentsManager_Manage_Add();
        }
    }

    public static void StudentsManager_Manage_Delete() {
        System.out.println("\t\t\tDeletion Actions!!");
        System.out.println("----Students List----");
        for (Document i: StudentsList) {
            System.out.println(pretty(i));
        }
        System.out.println("What type of deletion do you want? :\n\t a : Single Delete\n\t b : Bulk Delete\n\t x : Cancel");
        choice = scanner.next();
        if ("aA".contains(choice)) {
            System.out.print("Enter the uniqueID of the student to delete : ");
            String DelID = scanner.next();
            StudentCollection.deleteOne(Filters.eq("_id", DelID));
            System.out.println("Student with uniqueID " + DelID + " deleted");
        } else if ("bB".contains(choice)) {
            System.out.print("Choose the Delete Filters ");
            StudentCollection.deleteMany(Objects.requireNonNull(SearchFilters()));
            System.out.println("Bulk Delete Successful");
        } else if ("xX".contains(choice)) {
            StudentsManager();
        } else {
            System.err.println("Invalid Selection");
            StudentsManager_Manage_Delete();
        }
    }

    public static void StudentsManager_Manage_Update() {


        System.out.println("Choose any option :\n\t a : View all students and update\n\t b : Custom filter and update\n\t x : cancel");

        if ("aA".contains(choice)) {
            for (Document i: StudentsList) {
                System.out.println(pretty(i));
            }
            System.out.print("Enter the exact uniqueID of the object to select update : ");
            choice = scanner.next();
            StudentsManager_UpdateStudent();
        } else if ("bB".contains(choice)) {
            StudentManger_ViewStudentLis();
            // var UpdatesQueue = new ArrayList < Document > ();
            StudentsManager_UpdateStudent();
            System.out.println("Choose the scheme of updates :\n\t a : update/add single value\n\t b : update multiple values");
            choice = scanner.next();
            if ("aA".contains(choice)) {
                StudentsManager_UpdateStudent();
            } else if ("bB".contains(choice)) {
                choice = null;
                do {
                    StudentsManager_UpdateStudent();
                    System.out.print("Do you want to modify more Entities(students) :\n\t y : yes\n\tAny other key to cancel");
                    choice = scanner.next();
                } while ("yY".contains(choice));
            } else {
                System.out.println("Exiting...");
            }
        } else if ("xX".contains(choice)) {
            System.out.println("Cancelling...");
            StudentsManager_Manage();
        } else {
            System.out.println("Invalid Selection");
            StudentsManager_Manage_Update();
        }
    }

    public static Document StudentManager_DepartmentRetrieve(String SelectDepartment) {
        MongoCollection < Document > SelectedCollection = MainDatabase.getCollection("DegreesList");
        Bson projection = Projections.fields(Projections.include("Course_Name"), Projections.excludeId());
        FindIterable < Document > SelectedDocument = SelectedCollection.find(Filters.eq("_id", SelectDepartment)).projection(projection);

        for (Document i: SelectedDocument) {
            return i;
        }
        return null;
    }
    public static Document StudentManager_SectionsListRetrieve(String SelectDepartment) {
        MongoCollection < Document > SelectedCollection = MainDatabase.getCollection("DegreesList");
        Bson projection = Projections.fields(Projections.include("Sections"), Projections.excludeId());
        FindIterable < Document > SelectedDocument = SelectedCollection.find(Filters.eq("_id", SelectDepartment)).projection(projection);


//Oumar's Workspace Starting
public class creation
{
    public static void main(String[] args) throws InvalidFormatException, IOException {

        for (Document i: SelectedDocument) {
            return i;
        }
        return null;
    }
    // String SelectedDegree = null;
    //if (selectedDegDoc.contains("-" + SelectDepartment)) {
    //    int initIndex = selectedDegDoc.indexOf("-") - 2;
    //    SelectedDegree = selectedDegDoc.substring(initIndex, initIndex + 5);
    //}


    // Salman's Workspace Ending

    //Oumar's Workspace Starting
    public static void feeManagement() throws InvalidFormatException, IOException {
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
//Oumar's Workspace Endin

    //Humayun's Workspace Starting
    //Humaayun's Workspace Ending

    //Main Workspace(Collective)



    public static void main(String[] args) {

        Menu_main_Management();

        System.out.println("Collections Created Successfully");
        /*
         * Available collections :
         *       "StudentsManagement"
         *       "FeesManagement"
         *       "AttendanceManagement"
         *       "GradesManagement"*/

        // var docs = new ArrayList<Document>();//to Store and place documents at once
        // Document student = new Document("_id","FA21-BSE-077")
        //        .append("StudentName","Spec")
        //        .append("MarkSheet",asList(new Document("marks",10)
        //        .append("grade","A+")));
        //         docs.add(student);

        // Document Teacher  = new Document("_id", "077")
        //        .append("Teachername","Spec")
        //        .append("subejct", "PF")
        //        .append("semester", "2nd");
        //         docs.add(Teacher);

        // //Currentcollection.insertOne(CurrentDocument);
        // Currentcollection.insertMany(docs);
        // Bson globalFilter = Filters.eq("Teachername","NOT Spec");
        // Bson statusFilter = Filters.eq("StudentName", "NOT Spec");
        // FindIterable CurrentDoc =  Currentcollection.find();
        // Currentcollection.deleteOne(Filters.and(globalFilter,statusFilter));

        // System.out.print("Matcher running...");
        // String patternTemplate = "\\w\\w-?\\w\\w";
        // Pattern pattern = Pattern.compile(patternTemplate);
        // Matcher matcher = pattern.matcher(selectedDegDoc);
        // if (matcher.find()) {
        //    System.out.print (matcher.group()); // you can get it from desired index as well
        // } else {
        //    System.out.println("Error 404, Desired course string not found");
        // }
        // for(String Coll : MainDatabase.listCollectionNames()){
        //    System.out.println("\t" + Coll);

        // }
        // MongoCollection < Document > SelectedCollection =
        // MainDatabase.getCollection("StudentsManagement");
        //        Bson projection = Projections.fields(Projections.exclude("Course_Name"));
        //        FindIterable < Document > SelectedDocument =
        // SelectedCollection.find().projection(projection);
        //
        //        for(Document i : SelectedDocument){
        //            JSONObject json = new JSONObject(i);
        //            String Extracted_Degree = json.get("_id").toString();
        //             System.out.println(Extracted_Degree);
        //
        //        }
        //
    }
}
