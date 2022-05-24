//imports
import java.util.*; //to Import Every Library at once ArrayList, HashMaps, Scanners Used


//start of main class
public class MainTestPortalProject {

//global declarations
    static Scanner scanner = new Scanner(System.in);//Global Scanner For Inputs Anywhere inside program

    static Map<String, Map<String, String>> Major = new HashMap<>();//this will have te Student HashMap as Value to Course KEY and will return all the Students enrolled in the course
    static Map<String, String> Students = new HashMap<>();//This will Store the marks for Every student will a Unique Key(Roll Numbers)
    static Map<String,String> StudentMarks = new HashMap<>();//this will have same key as Students Hashmap

//Admin Section
    public static void AdminSection(Map<String, Map<String, String>> MajorPass,Map<String,String> StudentsPass){
        AdminStudentManager(MajorPass,StudentsPass);
        ///AdminQuizSection();
    }
    public static void AdminStudentManager(Map<String, Map<String, String>> MajorPass,Map<String,String> StudentsPass){
        System.out.print("Choose any option : \ta : Check Students List\tb : Manage Students\n");
        String choice = scanner.next();



        if("Aa".contains(choice)){
            System.out.println(MajorPass.toString());
        }else if("Bb".contains(choice)){
            System.out.println("Here's the list of Enrolled Students : \n\t" + MajorPass.toString());
            System.out.print("\n\n To select a Query to Edit Enter exact Query : ");
            String Deptt_Choice = scanner.next();
            System.out.print(MajorPass.get(Deptt_Choice));

            System.out.print("\n\nPress :\n\t a : Add Another Student\tb : Remove a Student\n\nc : cancel");
            choice = scanner.next();

            if("Aa".contains(choice)){
                System.out.print("Enter the UniqueID(RollNum) of the Student : ");
                String UniqueID = scanner.next();

                scanner.nextLine();//resetting scanner

                System.out.print("Enter the Details on the Student \t\t format : \" Name , Programme , Sem-(SemesterNumber) \"");
                String Details = scanner.nextLine();

                StudentsPass.put(UniqueID,Details);
                System.out.println("\n" + StudentsPass);

            }else if("Bb".contains(choice)){
                System.out.print("\n\n\tEnter the UniqueID(RollNum) of the Field(Student) to delete");
                String UniqueID = scanner.next();
                StudentsPass.remove(UniqueID);
                System.out.println(UniqueID + " has been deleted\n\t New List = " + StudentsPass);
                
            }else if("Cc".contains(choice)){
                AdminStudentManager(Major,Students);
            }
            AdminStudentManager(Major,Students);

        }
    }

    private static void AdminStudentManager() {
    }


    //Salman's Workspace
    public static void Quizz(){
        System.out.print("Choose any option : \ta : Students Section\tb : Teachers Section\n");
        String choice = scanner.next();
        if("Aa".contains(choice)){
            StudentQuizSection();
        }else if("Bb".contains(choice)){
            TeacherQuizSection();
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
//Oumar's Workspace Ending

//Humayun's Workspace Starting



//Humaayun's Workspace Ending

//Main Workspace(Collective)
    public static void main(String[] args) {

        ///Map<String, Object[]> Department = new HashMap<>();

        //ArrayList<Object> Programmes = new ArrayList<>();
        //static Map<String, Map<String, String>> Major = new HashMap<>();//this will have te Student HashMap as Value to Course KEY and will return all the Students enrolled in the course
        //static Map<String, String> Students = new HashMap<>();//This will Store the marks for Every student will a Unique Key(Roll Numbers)
        //static Map<String,String> StudentMarks = new HashMap<>();//this will have same key as Students Hashmap
        Students.put("BS-077","Salman, Software Engineering,Sem-2 ");
        Students.put("BSE-100", "Oumar, Software , SEM-2");
        Students.put("BSE-50","humayun,softwarem,SEM-2");
        for(String key : Students.keySet()){
        System.out.println(key + " : " + Students.get(key));
        }
        //Major.put("CompSc",Students);

        //AdminStudentManager(Major,Students);



    }
}
