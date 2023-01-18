# VICTA Mobile app for medical students

VICTA Mobile is a mobile and more handy version of a medical analysis tool meant for medical students to analyse and practice real life consultation scenarios using a software. The app is built using Java within Android Studio and is meant for Android mobile phones. This app was created as a group project with 5 students. Originally, we collaborated on GitLab but I exported our work to share with others.

## Main functionalities:
There are different screens for students and teachers. Students have lesser privileges than teachers within the app. Teachers have more power with sensitive information like adding patient details and creating scenarios as test questions for the students. Firebase Authentication is used to for correct authorisation of a user via their email.

### For students:
* View patient: Using this feature, a student can search for a patient name and view patient details before they start a scenario so they are equipped with background information of a particular patient before a consultation.
* Start scenario: Students are assessed using this functionality. A teacher creates a scenario code and sends it to their students. A student accesses a scenario by searching this code and accessing that scenario as their attempt. They can view a patient's symptoms and past diagnoses and treatments before they enter their own diagnosis for the patient. They also provide a treatment to the patient after diagnosis. Once the scenario is completed a summary with the user/student's inputs is generated.
* Send scenario: To submit a student's attempt for further marking, they can send the scenario summary to the teacher's allocated email. Then the teacher can review and mark it to end the process.

### For teachers (can also use all student features):
* Add patient: A new patient's details can be filled and submitted to firebase database. Then further scenarios can be created for this patient to diagnose and treat them.
* Add scenario: A teacher can search for a patient and set up a scenario by providing symptoms for an illness which is added to the firebase database.

## Scrum
Scrum was part of our agile development strategy we adopted for the project. I was the master for Sprint 1 and my notes detailing that experience can be found in ```Scrum_Master_Notes_Sprint1.md```. We also recorded the issues we created and the progress throughout all 4 sprints in ```/Scrum_Sprints/```.

## Where is the work located?
Our collective work can be found in the main branch by navigating to files/Sprint4_Final/AndroidDevelopment. All the files inside that directory are directly related to our program. The program can be run using Android Studio, by opening the AndroidDevelopment folder within the Android Studio and running 'app' in the project explorer.


## Installation instructions.

### PREREQUISITES:
* AndroidStudio (Latest) 
* Android Device emulator (can be downloaded from the device manager inside Android Studio) or a physical Android device.

### Steps to run the app
1. Download the git repository from the main branch.
2. Open Android Studio and then click on file/open. Navigate to the downloaded folder. Go to files/Sprint4_Final and click on AndroidDevelopment and open the project
3. All configurations are exported to Git so nothing should be changed.
4. On the left of the Android Studio, look at the project structure and right-click on "app" and click Run (top right green triangle button).
5. You should see a login page, There are different types of users: Use these credentials for a STUDENT account - U: testuser@le.ac.uk and P: test1234. Use these credentials for a TEACHER account - U: testteacher@gmail.com and P: test123.
6. If you are connected to the internet then you should successfully login and if you signed in as a STUDENT you will be redirected to the Student Main Menu or if you signed as a TEACHER you will go to Teacher Main Menu.
7. A teacher account has the ability to add patient, view patient, add scenario, start scenario and send scenario. Try to test all of these features as you wish. Once you add patient or scenario you can login in as a STUDENT to see if the changes have been made!
8. A student account has the ability to only view patient, start scenario and send scenario. Try to test all of these too. They are similar in both STUDENT and TEACHER account.
9. An Admin has to access firebase console and find a user and add a field with name: isTeacher and value: true. This would give TEACHER permissions to a basic user (STUDENT is a basic account).

### The following can be skipped but if the app does not run due to no Firebase Database connection, please follow these steps:
* Connection to firebase authentication: Go tools>firebase>authentication>authenticate using a custom authentication system>Follow the steps provided (At least 1 and 2).
* Connection to firebase realtime database: Go tools>firebase>realtime database>get started with realtime database>Follow the steps provided (At least 1 and 2).
