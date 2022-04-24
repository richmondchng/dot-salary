# dot-salary

Repository: dot-salary

Assumptions:
1. /users to return 0 to many records in the results field
2. Invalid parameters supplied for /users will return error 
	- min less than 0
	- max less than min
	- offset less than 0
	- limit less than 0
	- sort is not NAME or SALARY
3. /upload takes a text file in CSV format. Return success = 1 if succesful, 0 if failed
4. Invalid file uploaded to /upload will fail the entire process if
	- Not a text file
	- First row does not contain headers NAME and SALARY
	- Any row that does not contain only 2 values (NAME, and SALARY)
	- Any row that does not contain the expected data type; i.e., NAME (text), SALARY (numeric)
5. Row(s) that contains negative salary will be skipped

To Build:
1. Clone (download) repository
2. Open a command line terminal (e.g., PowerShell)
3. Navigate to project `root` folder
   -  e.g., `C:\dot-salary\dotsalary`
4. Run the following command to build project
   - `.\mvmw clean package`
5. When build is completed, run the following command to start application; include the file as the first parameter
   - `java -jar .\target\dotsalary-1.0-SNAPSHOT.jar`

Project Notes:
1. Built using Java 11
2. Require Java 11 to compile the project
3. Use the included maven wrapper to build the project
4. Additional dependencies
    - lombok
    - Apache common lang3
    - Junit Jupiter (for unit test)
    - Mockito (for unit test)

Project Structure:
1. Main class is at `richmond.swe.dotsalary.DotsalaryApplication`
2. Source classes are in `/src/main`
3. Package `richmond.swe.dotsalary.config` contains configuration and context building
4. Package `richmond.swe.dotsalary.controller` contains REST controller
5. Package `richmond.swe.dotsalary.service` contains service implementations
6. Package `richmond.swe.dotsalary.data` contains data classes, like entities and repositories
7. Package `richmond.swe.dotsalary.exception` contains Exception
8. Test classes are in `/src/test`
9. Sample CSV file in root directory `/sample-upload.csv`
10. POSTMAN collection in root directory `UserSalary.postman_collection.json`