**Hospital Appointment System**

Hospital Appointment System

Simple Java Swing app to manage doctors, patients, and appointments.

**Features:**
- Manage doctors and patients
- Create, view and cancel appointments
- Simple local database connection via `DBConnection`

**Prerequisites:**
- JDK 11 or newer installed and on your PATH
- (Optional) VS Code with the Java Extension Pack for easy development and running

**Folder structure:**
- `src/` — Java source files (package `hospital`)
- `lib/` — external libraries (if any)
- `bin/` — compiled classes (generated after building)

Key packages:
- `hospital.main` — contains `Main.java` (application entry point)
- `hospital.gui` — Swing UI frames
- `hospital.models`, `hospital.services`, `hospital.database` — core app logic and DB connection

**Build & Run (recommended: use an IDE)**
Run (IDE): open and run `hospital.main.Main`.

**Command-line (example):**
- Compile (from project root, requires shell that supports recursive globs or build via IDE):

	Windows (PowerShell with JDK in PATH):

	```powershell
	mkdir -Force bin
	Get-ChildItem -Path src -Recurse -Filter *.java | ForEach-Object { javac -d bin -cp "lib/*" $_.FullName }
	```

- Run the app:

	```powershell
	java -cp "bin;lib/*" hospital.main.Main
	```

**Notes:**
- The project was structured for educational use; adjust classpath or IDE run configuration as needed.

**Author:** Vishal (assignment)

Author: Vishal
