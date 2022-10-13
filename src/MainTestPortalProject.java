//imports

import java.util.*;
import java.util.logging.Logger; //To Disable MongoDB driver logs
import java.util.logging.Level; //To Disable MongoDB driver logs

//Oumar's imported libraries
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.result.UpdateResult;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.*;

import java.io.*;

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
import static com.mongodb.client.model.Updates.set;

//for Json Document extractions;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.lang.model.element.Name;

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
    System.out.println("Connecting...");
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
        System.out.println("Choose your Custom search : \n\ta : Greater Than\n\tb : Less Than\n\tc : Exact value \n\td : Specific range");
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

    //Pretty for formatting of Document being printed
    public static String pretty(Document document) {
        var settings = JsonWriterSettings.builder()
                .indent(true)
                .outputMode(JsonMode.SHELL)
                .build();
        return document.toJson(settings);
    }
    //function to call main menu everytime
    public static void Menu_main_Management() throws InterruptedException, IOException, InvalidFormatException {

        ConnectDB(); // this function will connect our program to our Atlas Cluster
        DisableMongoLogs(); //this will disable Annoying mongoDB logs
        initialize();
        System.out.println("ClearSC");
        MainScreen();

    }
    ///**Modules**
    //public Fee collection access
    public static MongoCollection < Document > FeeCollection = MainDatabase.getCollection("FeesManagement");
    public static FindIterable < Document > FeeList = FeeCollection.find();
    //public Attendance collection access
    public static MongoCollection < Document > AttendanceCollection = MainDatabase.getCollection("AttendanceManagement"); //opens Attendance management collection to work on
    public static FindIterable < Document > AttendanceList = AttendanceCollection.find();
    //public degree collection access
    public static MongoCollection < Document > DegreesCollection = MainDatabase.getCollection("DegreesList");
    public static FindIterable < Document > DegreesList = DegreesCollection.find();
    //public students collection access
    public static MongoCollection < Document > StudentCollection = MainDatabase.getCollection("StudentsManagement"); //open the Student  management collection to work on
    public static FindIterable < Document > StudentsList = StudentCollection.find(); //converts all documents in the Collection to iterable
    //public grades collection access
    public static MongoCollection < Document > GradesCollection = MainDatabase.getCollection("GradesManagement"); //open the Student  management collection to work on
    public static FindIterable < Document > GradesList = StudentCollection.find(); //converts all documents in the Collection to iterable

    public static void AttendanceStudentLinker(String subjectName) {
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

            String[] Alphabets = new String[] {
                    "A",
                    "B",
                    "C",
                    "D",
                    "E",
                    "F",
                    "G",
                    "H",
                    "I",
                    "J",
                    "K",
                    "L",
                    "M",
                    "N",
                    "O",
                    "P",
                    "Q",
                    "R",
                    "S",
                    "T",
                    "U",
                    "V",
                    "W",
                    "X",
                    "Y",
                    "Z"
            };
            List < String > SectionsGen = new ArrayList < > ();
            //String[] SubjectsList = new String[]{"",""};
            List < String > Subjects = new ArrayList < > ();

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

                DegreesCollection.insertOne(newDegree); //inserting the created Degree
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

    //main student manager module
    public static void StudentsManager() throws InterruptedException, IOException, InvalidFormatException {

        System.out.println("Choose any management operation :\n\t a : View Students\n\t b : Manage Students\n\t e : EXIT");
        String choice = scanner.next();

        if ("bB".indexOf(choice) == 0 || "bB".indexOf(choice) == 1) { //checked for B like this because of the bug
            StudentsManager_Manage();
        } else if ("Aa".contains(choice)) {
            StudentManger_ViewStudentList();
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
    public static void FeeManagement_ViewStudentFeeStatus() {
        System.out.println("Choose an option?\n\ta : view all students \n\tb : Custom search with Filters\n\t press Any other key to cancel");
        choice = scanner.next();
        if ("aA".contains(choice)) {
            for (Document i: FeeList) {
                System.out.println(pretty(i));
            }

        } else if ("bB".contains(choice)) {

            FeeList = FeeCollection.find().filter(SearchFilters()); //initializing the document again with filters
            for (Document i: FeeList) {
                System.out.println(pretty(i));
            }
        }
    }
    public static void StudentManger_ViewStudentList() {
        System.out.println("Choose an option?\n\ta : view all students \n\tb : Custom search with Filters\n\t press Any other key to cancel");
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
    public static void StudentsManager_UpdateStudent() throws InterruptedException, IOException, InvalidFormatException {
        System.out.print("Enter the exact uniqueID of the object to select update : ");
        String uniqueIDForUpdate = scanner.next();
        if (StudentCollection.find().filter(eq("_id", uniqueIDForUpdate)).first() == null) {
            boolean BadInput = true;
            while (BadInput) {
                System.err.println("Invalid ID!! please enter again");
                System.out.print("Enter the exact uniqueID of the object to select update : ");
                uniqueIDForUpdate = scanner.next();
                if (StudentCollection.find().filter(eq("_id", uniqueIDForUpdate)) == null) {
                    System.err.println("Invalid ID!!");
                } else {
                    BadInput = false;
                }
            }
        } else {
            for (Document i: StudentCollection.find().filter(eq("_id", uniqueIDForUpdate))) {
                System.out.print("Selected object : \n\t" + pretty(i));
            }
            do {
                System.out.print("Enter the exact name of field to update : ");
                String ObjectName = scanner.next();
                System.out.print("Enter the exact value you want to update : ");
                String ObjectVal = lineScanner.nextLine();
                System.out.print("Enter the new value to update \"" + ObjectVal + "\" to : ");
                String ObjectValUpdate = scanner.next();
                StudentCollection.updateOne(Filters.eq(ObjectName, ObjectVal), set(ObjectName, ObjectValUpdate));
                System.out.print("Make more changes to current selected object with ID " + uniqueIDForUpdate + " ?\n\t y : yes \n\t Any other key to cancel");
                choice = scanner.next();
                for (Document i: StudentCollection.find().filter(eq("_id", uniqueIDForUpdate))) {
                    System.out.print("Selected object : \n\t" + pretty(i));
                }
                System.out.print("Successfully updated!!");
            } while ("yY".contains(choice));
            StudentsManager_Manage_Update();
        }

    }

    public static void StudentsManager_Manage() throws InterruptedException, IOException, InvalidFormatException {
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

            Document SelectedDegreeObjForDept = StudentManager_CourseNamesRetrieve(SelectDepartment.toUpperCase());
            JSONObject SelectedDegreeObject = new JSONObject(SelectedDegreeObjForDept);
            String Extracted_Degree = SelectedDegreeObject.get("Course_Name").toString();

            String SelectedSection = StudentsManager_SelectSection(SelectDepartment);

            // System.out.println(SelectedDegreeSectionsObject);

            Document NewStudentData = new Document("_id", RegNum)
                    .append("StudentName", StudentName)
                    .append("FatherName", StudentFatherName)
                    .append("Department", SelectDepartment)
                    .append("Course_Name", Extracted_Degree)
                    .append("Class", SelectedSection);

            Document StudentDataForAttendance = new Document("_id", RegNum)
                    .append("Student Name", StudentName)
                    .append("Class", SelectedSection)
                    .append("Total Presents", 0)
                    .append("Total Absents", 0);

            Document StudentDataForFeeManagement = new Document("_id", RegNum)
                    .append("StudentName", StudentName)
                    .append("FatherName", StudentFatherName)
                    .append("Course_Name", Extracted_Degree)
                    .append("FeeStatus", null);

            Document StudentDataForGradesManagement = new Document("_id", RegNum)
                    .append("StudentName", StudentName)
                    .append("Department", SelectDepartment)
                    .append("Course_Name", Extracted_Degree)
                    .append("Class", SelectedSection);
            //Creating records(Document) for each new added student in other collections
            FeeCollection.insertOne(StudentDataForFeeManagement);
            AttendanceCollection.insertOne(StudentDataForAttendance);
            StudentCollection.insertOne(NewStudentData);
            GradesCollection.insertOne(StudentDataForGradesManagement);
            //prompting user
            System.out.println("Student Created : " + StudentName + " - " + RegNum);
            System.out.println("Add more students?\n\ta : yes\n\tAny other key to exit");
            choice = scanner.next();
            if ("aA".contains(choice)) {
                StudentsManager_Manage_Add();
            } else {
                System.out.println("Exiting...");
            }
        } else {
            System.err.println("Invalid RegNum" + RegNum);
            StudentsManager_Manage_Add();
        }
    }

    private static String StudentsManager_SelectSection(String SelectDepartment) {

        Document SelectedDegreeObjForSectionsList = StudentManager_SectionsListRetrieve(SelectDepartment.toUpperCase());
        JSONObject SelectedDegreeObjForSectionsListJson = new JSONObject(SelectedDegreeObjForSectionsList);
        JSONArray SelectedDegreeSectionsObject = (JSONArray) SelectedDegreeObjForSectionsListJson.get("Sections");


        System.out.println("--Sections--");
        ArrayList < Object > SectionsSelectionBuffer = new ArrayList < > ();
        int x = 1;
        for (Object i: SelectedDegreeSectionsObject) {
            System.out.println(x + " : " + i);
            x++;
            SectionsSelectionBuffer.add(i);
        }
        System.out.println("please enter Section's index to choose for the student : ");
        boolean BadInput = true;
        int SecSerial;
        String SelectedSection = null;
        while (BadInput) {
            try {
                SecSerial = IntScanner.nextInt();
                SelectedSection = (String) SectionsSelectionBuffer.get(SecSerial - 1);
                BadInput = false;
            } catch (InputMismatchException | IndexOutOfBoundsException e) {
                System.err.println("Invalid Input!! Please provide valid index : ");
            }
        }
        return SelectedSection;
    }

    public static void StudentsManager_Manage_Delete() throws InterruptedException, IOException, InvalidFormatException {
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

    public static void StudentsManager_Manage_Update() throws InterruptedException, IOException, InvalidFormatException {

        System.out.println("Choose any option :\n\t a : View all students and update\n\t b : Custom filter and update\n\t x : cancel");
        scanner.nextLine();
        choice = scanner.next();
        if ("aA".contains(choice)) {
            for (Document i: StudentsList) {
                System.out.println(pretty(i));
            }
            StudentsManager_UpdateStudent();
        } else if ("bB".contains(choice)) {
            StudentManger_ViewStudentList();

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

    public static Document StudentManager_CourseNamesRetrieve(String SelectDepartment) {
        MongoCollection < Document > SelectedCollection = MainDatabase.getCollection("DegreesList");
        Bson projection = Projections.fields(Projections.include("Course_Name"), Projections.excludeId());
        FindIterable < Document > SelectedDocument = SelectedCollection.find(Filters.eq("_id", SelectDepartment)).projection(projection);

        for (Document i: SelectedDocument) {
            return i;
        }
        return null;
    }


    public static Document StudentManager_SectionsListRetrieve(String SelectedDepartment) {
        MongoCollection < Document > SelectedCollection = MainDatabase.getCollection("DegreesList");
        Bson projection = Projections.fields(Projections.include("Sections"), Projections.excludeId());
        FindIterable < Document > SelectedDocument = SelectedCollection.find(Filters.eq("_id", SelectedDepartment.toUpperCase())).projection(projection);
        for (Document i: SelectedDocument) {
            return i;
        }
        return null;
    }

    public static String JSONobjectValuRetrieve(Document DocPassed, String ExactNameOfObjectToReturnValue) {
        Document SelectedObjForRetrieve = DocPassed;
        JSONObject SelectedObject = new JSONObject(SelectedObjForRetrieve);
        String Extracted_Value = SelectedObject.get(ExactNameOfObjectToReturnValue).toString();
        return Extracted_Value;
    }

    // Salman's Workspace Ending

    //Oumar's Workspace Starting
    public static void feeManagement(MongoDatabase MD, String D) throws InvalidFormatException, IOException, InterruptedException {

        List < String > chalanFileNames = new ArrayList < > ();
        if (D.equalsIgnoreCase("A")) {
            System.out.print(
                    "Enter \na: if you want to create a challan form\nb: if you want to issue chalan forms to the students\nc: If you want to check fee status\nd: If you want to update fee status");
            String choiceAdminF = scanner.next();
            if ("aA".contains(choiceAdminF)) {
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
            } else if ("bB".contains(choiceAdminF)) {
                List < String > DeegreesBuffer = new ArrayList < > ();
                int x = 0;
                System.out.println("--Degrees--");
                for (Document i: DegreesList) {
                    System.out.println((x + 1) + "- " + JSONobjectValuRetrieve(i, "_id"));
                    DeegreesBuffer.add(JSONobjectValuRetrieve(i, "_id"));
                    x++;
                }
                System.out.print("Enter Index of program of students to whom you want to issue chalan forms : ");
                int studentDegreeProgramIndex = IntScanner.nextInt();

                Map < String, String > studentsOfDegreeProgram = new HashMap < > ();
                FindIterable < Document > CustomStudentsList = StudentCollection.find(Filters.eq("Department", DeegreesBuffer.get(studentDegreeProgramIndex - 1)));
                System.out.println("--Selected Students--");
                for (Document i: CustomStudentsList) {
                    studentsOfDegreeProgram.put(JSONobjectValuRetrieve(i, "_id"), JSONobjectValuRetrieve(i, "StudentName"));
                    System.out.println((JSONobjectValuRetrieve(i, "_id") + "------" + JSONobjectValuRetrieve(i, "StudentName")));
                }

                //String[][] studentsOfDegreeProgram = {
                //        {
                //                "BSE-058",
                //                "Muhammad Oumar"
                //        },
                //        {
                //                "BSE-077",
                //                "Salman Tariq"
                //        },
                //        {
                //                "BSE-082",
                //                "Muhammad Humayun"
                //        }
                //}; /*The 2 statements can be replaced by mongoData for students in the departments*/
                //int rows = 3;
                //chalanFileNames = new String[rows];
                try {
                    System.out.println("Enter due date");
                    String dueDate = scanner.next();
                    System.out.println("Enter due fees");
                    String dueFees = scanner.next();
                    for (String key: studentsOfDegreeProgram.keySet()) {

                        XWPFDocument Document1 = new XWPFDocument(OPCPackage.open("SAMPLECHALAN.docx"));
                        for (XWPFTable table: Document1.getTables()) {
                            for (XWPFTableRow row: table.getRows()) {
                                for (XWPFTableCell cell: row.getTableCells()) {
                                    for (XWPFParagraph p: cell.getParagraphs()) {
                                        for (XWPFRun r: p.getRuns()) {
                                            String text = r.getText(0);
                                            if (text != null && text.contains("STUDENT NAME:")) {
                                                text = text.replace("STUDENT NAME:", key);
                                                r.setText(text, 0);
                                            }
                                            if (text != null && text.contains("ROLL NUMBER:")) {
                                                text = text.replace("ROLL NUMBER:", studentsOfDegreeProgram.get(key));
                                                r.setText(text, 0);
                                            }
                                            if (text != null && text.contains("PROGRAM:")) {
                                                text = text.replace("PROGRAM:", DeegreesBuffer.get(studentDegreeProgramIndex));
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
                                Document1.write(
                                        new FileOutputStream(
                                                key
                                                        .concat(studentsOfDegreeProgram.get(key))
                                                        .concat("banker")
                                                        .concat(".docx")));
                                /*The file is uploaded on the banker's module*/
                            } else {
                                Document1.write(
                                        new FileOutputStream(
                                                key
                                                        .concat(studentsOfDegreeProgram.get(key))
                                                        .concat(".docx")));
                                /*The file is uploaded on the student's module*/
                            }
                        }
                        chalanFileNames.add(key.concat(studentsOfDegreeProgram.get(key)).concat(".docx"));

                    }
                    System.out.println("Chalan forms have been created for the students");
                } catch (IOException e) {
                    System.out.println("e");
                }

            } else if ("cC".contains(choiceAdminF)) {
                MongoCollection < Document > feesManage = MainDatabase.getCollection("FeesManagement");
                System.out.println("Enter roll number of student to check fee status: ");
                String rollNumForFee = lineScanner.nextLine();
                List < Document > showFeeStatus = feesManage.find(eq("_id", rollNumForFee)).into(new ArrayList < > ());
                for (Document printFeeStatus: showFeeStatus) {
                    System.out.println(printFeeStatus.get("FeeStatus"));
                }
            } else if ("dD".contains(choiceAdminF)) {
                MongoCollection < Document > feesManageUpdate = MainDatabase.getCollection("FeesManagement");
                System.out.println("Enter roll number of student to update fee status");
                String rollNumForFeeUpdate = lineScanner.nextLine();
                System.out.println("Enter Student Fee Status ");
                String newFeeStatus = lineScanner.nextLine();
                Bson filterFee = eq("_id", rollNumForFeeUpdate);
                Bson setFeeStat = set("FeeStatus", newFeeStatus);
                UpdateResult updateFeeStat = feesManageUpdate.updateOne(filterFee, setFeeStat);
                System.out.println("Student's Fee Status Successfully Updated");

            }
        } else if (D.contains("S")) {

            MongoCollection < Document > feesManage = MainDatabase.getCollection("FeesManagement");
            System.out.println("Enter roll number of student to check fee status: ");
            String rollNumForFee1 = lineScanner.nextLine();
            List < Document > showFeeStatus1 = feesManage.find(eq("_id", rollNumForFee1)).into(new ArrayList < > ());
            for (Document printFeeStatus1: showFeeStatus1) {
                System.out.println(printFeeStatus1.get("FeeStatus"));
            }
        }
        System.out.println();
        System.out.println(".....................................................................................................................................................................................................");
        System.out.println();
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t PRESS M TO OPEN MAIN MENU ");
        String back_to_1 = scanner.next();
        if ("mM".contains(back_to_1)) {
            main_admin_menu(MD, D);
        }

    }
    //public static ArrayList < String > StudentsThatAppearedInTheQuizRollNumbers = new ArrayList < > ();
    //public static ArrayList < String > StudentsThatAppearedInTheQuizNames = new ArrayList < > ();

    public static void QuizManagement(String D) throws InterruptedException, IOException {

        String filename = "any";
        int totalQuestions = 15; /* can add input.nextInt()*/

        //Use this to transfer to GPA calculator
        double days1 = 0 * 24 * 60 * 60000;
        double hours1 = 0 * 60 * 60000;
        double minutes1 = 0 * 60000;
        double MainStartTimeQuiz = 1655050039699.0;
        double TimeGiven = days1 + hours1 + minutes1;

        String SelectedSection = null;
        String QuizNumber = null;
        if ("tT".contains(D)) {
            Bson projection = Projections.fields(Projections.include("_id"));
            FindIterable < Document > AvailableDepartmentList = DegreesCollection.find().projection(projection);
            List < String > DepartmentsStore = new ArrayList < > ();
            int x = 0;
            for (Document i: AvailableDepartmentList) {
                System.out.println((x + 1) + "- " + (JSONobjectValuRetrieve(i, "_id")));
                x++;
                DepartmentsStore.add((JSONobjectValuRetrieve(i, "_id")));
            }
            int departmentIndex;
            try {
                IntScanner.nextLine();
                System.out.println("Enter the department and the number of class that you want to issue the quiz");

                departmentIndex = IntScanner.nextInt();
                //System.out.println(JSONobjectValuRetrieve(StudentManager_SectionsListRetrieve(DepartmentsStore.get(departmentIndex-1)),"Sections"));
                SelectedSection = StudentsManager_SelectSection(DepartmentsStore.get(departmentIndex - 1));
                System.out.println("Enter the Quiz Number : ");
                QuizNumber = lineScanner.nextLine();

            } catch (InputMismatchException | IndexOutOfBoundsException e) {
                System.err.println("Bad Input!! Please enter valid Input again");
                QuizManagement("D");
            }
            //String SelectedSection = StudentsManager_SelectSection(Extracted_Degree);
            //FindIterable<Document> CustomStudentsListForNames = StudentCollection.find(Filters.eq("Class",SelectedSection)).projection(Projections.fields(Projections.include("StudentName"),Projections.excludeId()));
            //FindIterable<Document> CustomStudentsListForIDs = StudentCollection.find(Filters.eq("Class",SelectedSection));

            FindIterable < Document > CustomStudentsList = StudentCollection.find(Filters.eq("Class", SelectedSection));
            ArrayList < String > names = new ArrayList < > (); //,Projections ExactProjectionToFind
            ArrayList < String > rollNumbers = new ArrayList < > ();
            ArrayList < String > answers = new ArrayList < > ();
            ArrayList < Integer > qMarks = new ArrayList < > ();
            for (Document i: CustomStudentsList) {
                rollNumbers.add(JSONobjectValuRetrieve(i, "_id"));
                names.add(JSONobjectValuRetrieve(i, "StudentName"));
            }

            System.out.println(names + "  " + rollNumbers);

            //Using Mongo data, add students (and their roll numbers) of the respective dept and class to the names and rollNumbers array lists
            System.out.print("Enter \na: if you want to add a quiz and issue it to the students.\nb: if you want to check the quizes");
            String choiceTeacherQ = scanner.next();
            if (choiceTeacherQ.equalsIgnoreCase("a")) {
                try {
                    XWPFDocument Document5 = new XWPFDocument();
                    FileOutputStream SampleQuiz = new FileOutputStream("SampleQuiz3.docx");
                    Document5.write(SampleQuiz);
                    SampleQuiz.close();
                    System.out.println("A file has been created of name 'SampleQuiz' with .docx extension.");
                    System.out.println("Please now make a quiz on this docx file.");
                    System.out.print("If you have made the quiz, enter a to make answer key.");
                    String choice = scanner.next();
                    if ("aA".contains(choice)) {
                        XWPFDocument Document6 = new XWPFDocument(OPCPackage.open("SampleQuiz3.docx"));
                        for (int o = 0; o <= totalQuestions; o++) {
                            System.out.println("Please enter the answer for question " + o);
                            String sa = scanner.next();
                            for (XWPFParagraph paragraph3: Document6.getParagraphs()) {
                                for (XWPFRun r: paragraph3.getRuns()) {
                                    String text = r.getText(0);
                                    String concat1 = "Answer".concat(Integer.toString(o + 1)).concat(":");
                                    if (text != null && text.contains(concat1)) {
                                        text = text.replace(concat1, concat1.concat(sa));
                                        r.setText(text, 0);
                                        try {
                                            FileOutputStream fos = new FileOutputStream

                                                    ("D:\\Java-Sem2-Project\\mynew.txt", true);
                                            PrintStream writer = new PrintStream(fos);
                                            String anyanswer = concat1.concat(sa);
                                            writer.println(anyanswer);
                                            writer.close();
                                        } catch (Exception e) {
                                            System.out.println("An error has occurred.");
                                        }
                                    }
                                }
                            }
                        }
                        Document6.write(new FileOutputStream("SampleQuizAnswerKey1.docx"));
                        System.out.println("Another file has been created as answer key of name 'SampleQuizAnswerKey' with .docx extension.");
                    }
                } catch (IOException | InvalidFormatException e) {
                    System.out.println(e);
                }
                for (int l = 0; l < names.size(); l++) {
                    try {
                        filename = names.get(l).concat(rollNumbers.get(l)).concat(".docx");
                        XWPFDocument Document7 = new XWPFDocument(OPCPackage.open("SampleQuiz3.docx"));
                        Document7.write(new FileOutputStream(filename));
                        /*send this file to MongoDataBase to the respective student of this roll number*/
                    } catch (IOException | InvalidFormatException e) {
                        System.out.println(e);
                    }
                }
                System.out.println("Quizes have been uploaded.");
                System.out.println("Edit the code for time for submission in days,hours,minutes.");
            } else if (choiceTeacherQ.equalsIgnoreCase("b")) {
                if (System.currentTimeMillis() - MainStartTimeQuiz >= TimeGiven) {
                    try {
                        int sMarks = 0;
                        for (int m = 0; m < names.size(); m++) {
                            sMarks = 0;
                            XWPFDocument docx = new XWPFDocument(new FileInputStream(rollNumbers.get(m).concat(names.get(m)).concat("1.docx")));
                            XWPFWordExtractor wordFile = new XWPFWordExtractor(docx);
                            String ultimate = wordFile.getText();
                            ArrayList < String > mm = new ArrayList < String > ();
                            try {
                                File file = new File("D:\\Java-Sem2-Project\\mynew.txt");
                                Scanner input1 = new Scanner(file);
                                for (int count1 = 0; count1 <= 14; count1++) {
                                    String anyanswer = input1.next();
                                    mm.add(anyanswer);
                                }
                                input1.close();
                            } catch (Exception e) {
                                System.out.println("An error has occurred." + e.toString());
                            }
                            for (int n = 0; n <= 14; n++) {
                                if (ultimate.contains(mm.get(n))) {
                                    sMarks = sMarks + 1;
                                } else
                                    continue;
                            }
                            //    qMarks.set(m, sMarks);
                            System.out.println(sMarks);

                        }
                        int z = 0;
                        for (String key: rollNumbers) {
                            Bson filter1 = eq("_id", key);
                            Bson updatedOperation = set("Quiz_1", qMarks.get(z));
                            UpdateResult ur = GradesCollection.updateOne(filter1, updatedOperation);

                            //   GradesCollection.updateOne(Filters.eq("_id", i),new Document("Quiz_1",qMarks.get(z)));
                            z++;
                        }

                        System.out.println("Student's marks for " + QuizNumber + " : " + sMarks);
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                } else
                    System.out.println("Time given has not finished yet");
            }
        } else if ("sS".contains(D)) {
            String rNumber = null;
            boolean WrongInput = true;
            while (WrongInput) {
                System.out.print("Enter your roll number please in format [AAA-AAA]");
                rNumber = scanner.next();
                if (rNumber.charAt(3) == '-' && rNumber.length() == 7) {
                    WrongInput = false;
                } else if (GradesCollection.find(Filters.eq("_id", rNumber)) == null) {
                    System.err.println("The Roll Number " + rNumber + " does not exist, PLEASE ENTER A VALID INPUT!!");
                } else {
                    System.err.println("Bad Input!! please enter again");
                }
            }

            System.out.print("Enter your name please");
            String studentName = scanner.next();
            if (System.currentTimeMillis() - MainStartTimeQuiz < TimeGiven) {
                try {
                    XWPFDocument Document9 = new XWPFDocument(OPCPackage.open(studentName.concat(rNumber).concat(".docx")));
                    double starttime = System.currentTimeMillis();
                    if (System.currentTimeMillis() - starttime < 900000) {
                        for (int o = 1; o <= totalQuestions; o++) {
                            if (System.currentTimeMillis() - starttime < 900000) {
                                System.out.println("Please enter the answer for question " + o);
                                if (System.currentTimeMillis() - starttime < 900000) {
                                    String studentAnswer = scanner.next();
                                    if (System.currentTimeMillis() - starttime < 900000) {
                                        for (XWPFParagraph paragraph3: Document9.getParagraphs()) {
                                            for (XWPFRun r: paragraph3.getRuns()) {
                                                String text = r.getText(0);
                                                String concat1 = "Answer".concat(Integer.toString(o)).concat(":");
                                                if (text != null && text.contains(concat1)) {
                                                    text = text.replace(concat1, concat1.concat(studentAnswer));
                                                    r.setText(text, 0);
                                                }
                                            }
                                        }
                                    } else {
                                        System.out.println("Time is over");
                                        break;
                                    }
                                } else {
                                    System.out.println("Time is over");
                                    break;
                                }
                            } else {
                                System.out.println("Time is over");
                                break;
                            }
                        }
                        Document9.write(new FileOutputStream(rNumber.concat(studentName).concat("1.docx")));
                    }
                } catch (IOException | InvalidFormatException e) {
                    System.out.print("e");
                }
            } else {
                System.out.println("Sorry! You are late than the given time");
            }
        }
    }
    public static void AssignmentModule(String user_type) {

        String filenameA = "any";

        Map < String, String > StudentsBuffer = new HashMap < > ();
        Map < String, String > StudentsMarksBuffer = new HashMap < > ();

        double days2 = 7 * 24 * 60 * 60000;
        double hours2 = 0 * 60 * 60000;
        double minutes2 = 0 * 60000;
        double MainStartTimeAssignment = 1655050039699.0;
        double Deadline = days2 + hours2 + minutes2;
        if ("tT".contains(user_type)) {
            String SelectedSection;
            String departmentNameA = null;
            Bson projection = Projections.fields(Projections.include("_id"));
            FindIterable < Document > AvailableDepartmentList = DegreesCollection.find().projection(projection);
            List < String > DepartmentsStore = new ArrayList < > ();
            int x = 0;
            System.out.println("Please specify Assignment's Number");
            String AssignmentNum = scanner.next();
            for (Document i: AvailableDepartmentList) {
                System.out.println((x + 1) + "- " + (JSONobjectValuRetrieve(i, "_id")));
                x++;
                DepartmentsStore.add((JSONobjectValuRetrieve(i, "_id")));
            }
            int departmentIndex;
            try {
                IntScanner.nextLine();
                System.out.println("Enter the department and the number of class that you want to issue the Assignment");

                departmentIndex = IntScanner.nextInt();
                //System.out.println(JSONobjectValuRetrieve(StudentManager_SectionsListRetrieve(DepartmentsStore.get(departmentIndex-1)),"Sections"));
                SelectedSection = StudentsManager_SelectSection(DepartmentsStore.get(departmentIndex - 1));
                departmentNameA = SelectedSection;
            } catch (InputMismatchException | IndexOutOfBoundsException e) {
                System.err.println("Bad Input!! Please enter valid Input again");
                AssignmentModule("D");
            }
            //System.out.println("Enter the department and the class that you want to issue the assignment");
            FindIterable < Document > StudentsListBuffer = StudentCollection.find(Filters.eq("Department", departmentNameA));
            for (Document i: StudentsListBuffer) {
                StudentsBuffer.put(JSONobjectValuRetrieve(i, "_id"), JSONobjectValuRetrieve(i, "StudentName"));
                System.out.print(JSONobjectValuRetrieve(i, "_id") + "--------" + JSONobjectValuRetrieve(i, "StudentName"));
            }
            //Using Mongo data, add students (and their roll numbers) of the respective dept and class to the names and rollNumbers array lists
            System.out.print("Enter \na: if you want to add an assignment and issue it to the students.\nb: if you want to upload marks for assignments");
            String choiceTeacherA = scanner.next();
            if (choiceTeacherA.equalsIgnoreCase("a")) {
                try {
                    XWPFDocument Document = new XWPFDocument();
                    FileOutputStream Assignment = new FileOutputStream(new File("BASEASSIGNMENT.docx"));
                    Document.write(Assignment);
                    Assignment.close();
                    System.out.println("A file has been created of name BASEASSIGNMENT' with .docx extension.");
                    System.out.println("Please now make an assignment on this docx file.");
                } catch (Exception e) {
                    System.out.println(e);
                }
                for (String key: StudentsBuffer.keySet()) {

                    try {
                        filenameA = StudentsBuffer.get(key).concat(key.concat(".docx"));
                        XWPFDocument Document1 = new XWPFDocument(OPCPackage.open("BASEASSIGNMENT.docx"));
                        Document1.write(new FileOutputStream(filenameA));
                        /*send this file to MongoDataBase to the respective student of this roll number*/
                    } catch (InvalidFormatException | IOException e) {
                        System.out.println(e);
                    }
                }
                for (String key: StudentsBuffer.keySet()) {
                    try {
                        String filename = StudentsBuffer.get(key).concat(key).concat(".docx");
                        XWPFDocument Document7 = new XWPFDocument(OPCPackage.open("SampleQuiz3.docx"));
                        Document7.write(new FileOutputStream(filename));
                        /*send this file to MongoDataBase to the respective student of this roll number*/
                    } catch (IOException | InvalidFormatException e) {
                        System.out.println(e);
                    }
                }
                System.out.println("Quizes have been uploaded.");
                System.out.println("Edit the code for time for submission in days,hours,minutes.");
                System.out.println("Assignments have been uploaded.");
                System.out.println("Edit the code by editing the mainStartTimeAssignment, days, hours and minutes for deadline.");
            } else if (choiceTeacherA.equalsIgnoreCase("b")) {
                if (System.currentTimeMillis() - MainStartTimeAssignment >= Deadline) {
                    for (String key: StudentsBuffer.keySet()) {
                        System.out.println("upload the marks of " + StudentsBuffer.get(key));
                        String marksNameA = lineScanner.nextLine();
                        StudentsMarksBuffer.put(key, marksNameA);
                        GradesCollection.updateOne(Filters.eq("_id", key), new Document(("Assignment_" + AssignmentNum), StudentsBuffer.get(key)));
                    }
                }
            }
        } else if ("sS".contains(user_type)) {
            System.out.print("Enter your roll number please");
            String rNumberA = scanner.next();
            System.out.print("Enter your name please");
            String studentNameA = scanner.next();
            if (System.currentTimeMillis() - MainStartTimeAssignment < Deadline - (10 * 60000)) {
                try {
                    XWPFDocument Document2 = new XWPFDocument(OPCPackage.open(studentNameA.concat(rNumberA).concat(".docx")));
                    for (XWPFParagraph paragraph2: Document2.getParagraphs()) {
                        for (XWPFRun r: paragraph2.getRuns()) {
                            String text = r.getText(0);
                            if (text != null && text.contains("Roll Number:")) {
                                text = text.replace("Roll Number:", "BSE-058");
                                r.setText(text, 0);
                            }
                            if (text != null && text.contains("Name:")) {
                                text = text.replace("Name:", "Muhammad Oumar");
                                r.setText(text, 0);
                            }
                        }
                    }
                    Document2.write(new FileOutputStream(rNumberA.concat(studentNameA).concat("1.docx")));
                } catch (InvalidFormatException | IOException e) {
                    System.out.println(e);
                }
            } else {
                System.out.println("Sorry! You are late than the given time");
            }
        }
    }
    //Oumar's Workspace Ending

    //Humayun's Workspace Starting
    public static void MainScreen() throws IOException, InterruptedException, InvalidFormatException {
        loginScreen(MainDatabase);
    }
    public static void loginScreen(MongoDatabase MD) throws IOException, InterruptedException, InvalidFormatException {
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t UNIVERSITY WEB PORTAL ");
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("> NOTICEBOARD");
        System.out.println();
        System.out.print("\t\t\t1- FALL'22 Admissions Are Open For Undergraduate Programs. Admission tests are starting from June 25, 2022.\n\t\t\t2- Registrations For Ehasaas Scholarhip Has Been Initiated. Relevant Students Can Visit Admin Office.\n\t\t\t3- EVENT : University Is Organizing A 15 Days Workshop On Emerging Technologies.\n\t\t\t4- [EXHIBITION] : University Is Conducting Project Exhibition & Interview Programs For Final Semester Students\n\t\t\t\tComplete Details Will Be Discussed By Department Heads.  \n");
        System.out.println();
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println();
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tSTUDENT LOGIN - Press S\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tTEACHING FACULTY LOGIN - Press T\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tADMIN LOGIN - Press A");

        String main_portal_input = scanner.next();
        if ("sS".contains(main_portal_input)) {

            boolean passVerify = false;
            do {
                System.out.println("Enter your 5 digit student ID");
                int studentLoginId = IntScanner.nextInt(); // STUDENT LOGIN ID INPUT
                String studentID = String.valueOf(studentLoginId);
                if (studentID.length() == 5) {
                    passVerify = true;
                    main_student_menu(MD, main_portal_input);
                } else
                    System.out.println("You entered in-correct password ");
            } while (passVerify == false);

        } else if ("tT".contains(main_portal_input)) {
            boolean passVerify = false;
            do {
                System.out.println("Enter your 5 digit teacher ID");
                int teacherLoginID = IntScanner.nextInt(); // TEACHER LOGIN ID INPUT
                String teacherID = String.valueOf(teacherLoginID);
                if (teacherID.length() == 5) {
                    passVerify = true;
                    main_teacher_menu(MD, main_portal_input);
                } else
                    System.out.println("You entered in-correct password ");
            } while (passVerify == false);
        } else if ("aA".contains(main_portal_input)) {
            boolean passVerify = false;
            do {
                System.out.println("Enter your 5 digit admin ID");
                int adminLoginId = IntScanner.nextInt(); // ADMIN LOGIN ID INPUT
                String adminID = String.valueOf(adminLoginId);
                if (adminID.length() == 5) {
                    passVerify = true;
                    main_admin_menu(MD, main_portal_input);
                } else
                    System.out.println("You entered in-correct password ");
            } while (passVerify == false);
        }

    }

    public static void main_student_menu(MongoDatabase MD, String user_type) throws IOException, InterruptedException, InvalidFormatException {
        Scanner input = new Scanner(System.in);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t STUDENTS CONSOLE ");
        System.out.println("======================================================================================================================================================================================================");
        System.out.println();
        System.out.println("1- Attendance Management | Press A\n2- Fees Management | Press B\n3- Assignments Management | Press C\n4- Quiz Management | Press D");
        System.out.println();
        System.out.println(".....................................................................................................................................................................................................");
        System.out.println();
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t PRESS L TO LOGOUT ");
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t TYPE 'MESSAGE' TO CONTACT US ");
        String mainStudentInput;
        Boolean BadInput = true;
        while (BadInput) {
            try {
                mainStudentInput = scanner.next();
                BadInput = true;
                if ("aA".contains(mainStudentInput)) {
                    showAttendance(MD, user_type);
                } else if ("bB".contains(mainStudentInput)) {
                    feeManagement(MD, user_type);
                } else if ("cC".contains(mainStudentInput)) {
                    AssignmentModule(user_type);
                } else if ("dD".contains(mainStudentInput)) {
                    QuizManagement(user_type);
                } else if ("message".equalsIgnoreCase(mainStudentInput)) {
                    messageWrite(MD, user_type);
                } else if ("l".equalsIgnoreCase(mainStudentInput)) {
                    loginScreen(MD);
                } else {
                    System.err.println("Input Invalid!!, You may select again");
                    main_student_menu(MD, user_type);
                }

            } catch (InputMismatchException e) {
                System.out.println("Input Invalid!!, You may select again");
                main_student_menu(MD, user_type);

            }

        }

    }
    public static void main_teacher_menu(MongoDatabase MD, String user_type) throws IOException, InterruptedException, InvalidFormatException {
        Scanner input = new Scanner(System.in);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t TEACHING FACULTY CONSOLE ");
        System.out.println("======================================================================================================================================================================================================");
        System.out.println();
        System.out.println("1- Attendance Management | Press A\n2- Assignments Management | Press B\n3- Quiz Management | Press C\n4- Contact Admin | Press D");
        System.out.println();
        System.out.println(".....................................................................................................................................................................................................");
        System.out.println();
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t PRESS L TO LOGOUT ");
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t TYPE 'MESSAGE' TO CONTACT US ");
        String mainTeacherInput;
        Boolean BadInput = true;
        while (BadInput) {
            try {
                mainTeacherInput = scanner.next();
                BadInput = true;
                if ("aA".contains(mainTeacherInput)) {
                    showAttendance(MD, user_type);
                } else if ("bB".contains(mainTeacherInput)) {
                    AssignmentModule(user_type);
                } else if ("cC".contains(mainTeacherInput)) {
                    QuizManagement(user_type);
                } else if ("dD".contains(mainTeacherInput)) {
                    messageWrite(MD, user_type);
                } else if ("message".equalsIgnoreCase(mainTeacherInput)) {
                    messageWrite(MD, user_type);
                } else if ("l".equalsIgnoreCase(mainTeacherInput)) {
                    loginScreen(MD);
                } else {
                    System.err.println("Input Invalid!!, You may select again");
                    main_teacher_menu(MD, user_type);
                }

            } catch (InputMismatchException e) {
                System.out.println("Input Invalid!!, You may select again");
                main_teacher_menu(MD, user_type);

            }

        }

    }

    public static void main_admin_menu(MongoDatabase MD, String user_type) throws IOException, InterruptedException, InvalidFormatException {
        Scanner input = new Scanner(System.in);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t ADMIN CONSOLE ");
        System.out.println("======================================================================================================================================================================================================");
        System.out.println();
        System.out.println("1- Students Management | Press A\n2- Fees Management | Press B\n3- Degree Management - Press C\n4- Check Inbox | Press D");
        System.out.println();
        System.out.println(".....................................................................................................................................................................................................");
        System.out.println();
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t PRESS L TO LOGOUT ");
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t TYPE 'MESSAGE' TO CONTACT US ");
        System.out.println();
        String mainAdminInput;
        Boolean BadInput = true;
        while (BadInput) {
            try {
                mainAdminInput = scanner.next();
                BadInput = true;
                if ("aA".contains(mainAdminInput)) {
                    showAttendance(MD, user_type);
                } else if ("bB".contains(mainAdminInput)) {
                    feeManagement(MD, user_type);
                } else if ("cC".contains(mainAdminInput)) {
                    Degree_manager();
                } else if ("dD".contains(mainAdminInput)) {
                    messageRead();
                } else if ("message".equalsIgnoreCase(mainAdminInput)) {
                    messageWrite(MD, user_type);
                } else if ("l".equalsIgnoreCase(mainAdminInput)) {
                    loginScreen(MD);
                } else {
                    System.err.println("Input Invalid!!, You may select again");
                    main_admin_menu(MD, user_type);
                }

            } catch (InputMismatchException e) {
                System.out.println("Input Invalid!!, You may select again");
                main_admin_menu(MD, user_type);

            }
        }

    }
    public static void messageWrite(MongoDatabase MD, String user_type) {

        try {
            FileWriter f1 = new FileWriter("D:contactus.txt", true);
            System.out.println("< Enter Your Name >");
            String nameForMsg = lineScanner.nextLine();
            System.out.println("< Enter Your Email >");
            String emailForMsg = lineScanner.nextLine();
            System.out.println("< Enter Your Message Below >");
            String message = lineScanner.nextLine();
            f1.write("Sender Name: " + nameForMsg + "\n");
            f1.write("Sender's email: " + emailForMsg + "\n");
            f1.write(message + "\n" + "--------------------------- NEXT MESSAGE ------------------------------------\n");
            System.out.println();
            System.out.println("Your Message Is Sent Successfully ");
            f1.close();
            System.out.println(".....................................................................................................................................................................................................");
            System.out.println();
            System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t PRESS B TO NAVIGATE BACK");
            String back_to_15 = lineScanner.nextLine();
            if ("bB".contains(back_to_15))
                main_admin_menu(MD, user_type);

        } catch (Exception e1) {
            System.out.println(e1);
        }

    }
    public static void messageRead() {
        File file1 = new File("D:contactus.txt");

        try {
            Scanner sc = new Scanner(file1);
            while (sc.hasNext()) {
                String read = sc.nextLine();
                System.out.println(read);
            }
            sc.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public static void attendanceMain(MongoDatabase MD, String user_type) throws IOException, InterruptedException, InvalidFormatException {
        Scanner input = new Scanner(System.in);
        System.out.println(
                "----------------------------------------------------\nCLASS ATTENDANCE MANAGEMENT\n----------------------------------------------------");
        System.out.println("Take Attendance - Press A");
        System.out.println("View Attendance Report - Press B");
        System.out.println("Update Student Attendance - Press C\n");

        String teacher_atd_option = scanner.next();
        if ("aA".contains(teacher_atd_option)) {
            takeAttendance(MD, user_type);
        } else if ("bB".contains(teacher_atd_option)) {
            showAttendance(MD, user_type);
        } else if ("cC".contains(teacher_atd_option)) {
            update_std_attendance(MD, user_type);
        }
        System.out.println();
        System.out.println(".....................................................................................................................................................................................................");
        System.out.println();
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t PRESS M TO OPEN MAIN MENU ");
        String back_to_1 = scanner.next();
        if ("mM".contains(back_to_1)) {
            main_teacher_menu(MD, user_type);
        }

    }

    public static void takeAttendance(MongoDatabase MD, String user_type) throws IOException, InterruptedException, InvalidFormatException {

        MongoCollection < Document > collection = MD.getCollection("AttendanceManagement");
        Scanner input = new Scanner(System.in);
        System.out.println();
        System.out.print("Enter Class (Format: DD-S) -> ");
        String class_of_attendance = lineScanner.nextLine();
        List < Document > nameList = collection.find(eq("Class", class_of_attendance)).into(new ArrayList < > ());
        System.out.print("Enter the date of attendance -> ");
        System.out.println();
        String wr_atd_date = lineScanner.nextLine();
        System.out.println("For Present - Press P ");
        System.out.println("For Absent - Press A ");
        System.out.println();
        for (Document studentAtdReport: nameList) {
            String wr_roll_num = String.valueOf(studentAtdReport.get("_id"));
            String wr_presents = String.valueOf(studentAtdReport.get("Total Presents"));
            String wr_absents = String.valueOf(studentAtdReport.get("Total Absents"));
            int wr_std_presents = Integer.parseInt(wr_presents);
            int wr_std_Absents = Integer.parseInt(wr_absents);
            System.out.println("Roll number: " + wr_roll_num);
            String atd_input = lineScanner.nextLine();
            if ("pP".contains(atd_input)) {
                Bson filter = eq("_id", wr_roll_num);
                Bson updatePresents = and(set("Total Presents", (wr_std_presents + 1)), set(wr_atd_date, "Present"));
                UpdateResult ur1 = collection.updateMany(filter, updatePresents);
            } else if ("aA".contains(atd_input)) {
                Bson filter = eq("_id", wr_roll_num);
                Bson updateAbsents = and(set("Total Absents", (wr_std_Absents + 1)), set(wr_atd_date, "Absent"));
                UpdateResult ur2 = collection.updateMany(filter, updateAbsents);
            }

        }

        List < Document > studentList = collection.find().into(new ArrayList < > ());
        for (Document x3: studentList) {
            System.out.println(pretty(x3));
        }
        System.out.println("Success: The attendance has been recorded ");
        System.out.println();
        System.out.println(".....................................................................................................................................................................................................");
        System.out.println();
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t PRESS B TO NAVIGATE BACK");
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t PRESS M TO OPEN MAIN MENU ");
        String back_to_1 = lineScanner.nextLine();
        if ("bB".contains(back_to_1))
            attendanceMain(MD, user_type);
        else if ("mM".contains(back_to_1))
            main_teacher_menu(MD, user_type);

    }
    public static void showAttendance(MongoDatabase MD, String user_type) throws IOException, InterruptedException, InvalidFormatException {
        Scanner input = new Scanner(System.in);
        System.out.println("VIEW ATTENDANCE REPORT\n---------------------------");
        System.out.print("Enter Student Roll Number: ");
        String std_roll_for_atdview = lineScanner.nextLine();
        System.out.println();
        System.out.println("1- Complete Attendance Report ");
        System.out.println("2- Specific Date Attendance ");
        int atd_view_teacher = IntScanner.nextInt();;
        if (atd_view_teacher == 1) {
            view_attendance_complete(MD, std_roll_for_atdview, user_type);
        } else if (atd_view_teacher == 2) {
            view_attendance_specific(MD, std_roll_for_atdview, user_type);
        }
    }

    public static void view_attendance_complete(MongoDatabase MD, String std_rollNum, String user_type) throws IOException, InterruptedException, InvalidFormatException {
        Scanner input = new Scanner(System.in);
        MongoCollection < Document > collection = MD.getCollection("AttendanceManagement");
        FindIterable < Document > getStudents = collection.find(Filters.eq("_id", std_rollNum));
        for (Document dx2: getStudents) {
            System.out.println(pretty(dx2));
        }
        System.out.println();
        System.out.println(".....................................................................................................................................................................................................");
        System.out.println();
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t PRESS B TO NAVIGATE BACK");
        String back_to_x = lineScanner.nextLine();
        if ("bB".contains(back_to_x) && "sS".contains(user_type)) {
            main_student_menu(MD, user_type);
        } else if ("bB".contains(back_to_x) && "Tt".contains(user_type)) {
            main_teacher_menu(MD, user_type);
        }

    }
    public static void view_attendance_specific(MongoDatabase MD, String std_rollNum, String user_type) throws IOException, InterruptedException, InvalidFormatException {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter date to fetch attendance: ");
        String date_atd_view = lineScanner.nextLine();
        MongoCollection < Document > collection = MD.getCollection("AttendanceManagement");
        FindIterable < Document > getStudents2 = collection.find(Filters.eq("_id", std_rollNum));
        for (Document dx3: getStudents2) {
            String attendanceStatus = String.valueOf(dx3.get(date_atd_view));
            System.out.println("Attendance Status: " + attendanceStatus);
        }
        System.out.println();
        System.out.println(".....................................................................................................................................................................................................");
        System.out.println();
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t PRESS B TO NAVIGATE BACK");
        String back_to_xy = lineScanner.nextLine();
        if ("bB".contains(back_to_xy) && "sS".contains(user_type)) {
            main_student_menu(MD, user_type);
        } else if ("bB".contains(back_to_xy) && "Tt".contains(user_type)) {
            main_teacher_menu(MD, user_type);
        }

    }

    public static void update_std_attendance(MongoDatabase MD, String user_type) throws IOException, InterruptedException, InvalidFormatException {
        Scanner input = new Scanner(System.in);
        MongoCollection collection = MD.getCollection("AttendanceManagement");
        System.out.println("UPDATE STUDENT ATTENDANCE\n---------------------------");
        System.out.print("Enter Roll Number Of Student: ");
        String std_roll_for_update = lineScanner.nextLine(); // ROLL NUMBER OF STUDENT TO UPDATE ATTENDANCE
        System.out.print("Enter Date Of Attendance: ");
        String atd_date_update = lineScanner.nextLine(); // Date of attendance
        System.out.print("Change Attendance Status To: P / A ");
        String atd_update_status = lineScanner.nextLine(); // NEW ATTENDANCE STATUS
        FindIterable < Document > class_Students = collection.find(Filters.eq("_id", std_roll_for_update));

        for (Document studentAtdReport: class_Students) {
            String presents = String.valueOf(studentAtdReport.get("Total Presents"));
            String absents = String.valueOf(studentAtdReport.get("Total Absents"));
            Integer t_presents = Integer.valueOf(presents);
            Integer t_absents = Integer.valueOf(absents);

            if ("pP".contains(atd_update_status)) {
                Bson filter = eq("_id", std_roll_for_update);
                Bson update_atd_pr = and(set(atd_date_update, "Present"), set("Total Presents", (t_absents - 1)));
                UpdateResult ur2 = collection.updateMany(filter, update_atd_pr);
            } else if ("aA".contains(atd_update_status)) {
                Bson filter = eq("_id", std_roll_for_update);
                Bson update_atd_ab = and(set("Total Absents", (t_presents - 1)), set(atd_date_update, "Absent"));
                UpdateResult ur2 = collection.updateMany(filter, update_atd_ab);
            }
            System.out.println("Total Presents: " + t_presents + "  ||  " + "Total absents " + t_absents);
            System.out.println();

        }
        System.out.println("The student attendance has been updated! ");
        System.out.println();
        System.out.println(".....................................................................................................................................................................................................");
        System.out.println();
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t PRESS B TO NAVIGATE BACK");
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t PRESS M TO OPEN MAIN MENU ");
        String back_to_5 = lineScanner.nextLine();
        if ("bB".contains(back_to_5))
            attendanceMain(MD, user_type);
        else if ("mM".contains(back_to_5))
            main_teacher_menu(MD, user_type);

    }
    //Humaayun's Workspace Ending

    //Main Workspace(Collective)

    public static void main(String[] args) throws InterruptedException, IOException, InvalidFormatException {

        Menu_main_Management();
    }
}
