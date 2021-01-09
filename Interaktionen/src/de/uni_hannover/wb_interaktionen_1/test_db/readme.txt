Setup Database:

    Download the MySQL Server and Workbench and set up a database by choosing a keyword (remember that one).
    Then go to workbench and connect to the DB (click the server).
    In the navigation bar choose: Server -> Import data
    Now "Import from Self-Contained File", select or paste the path to the .sql file
    Hit "start import" in the lower right corner.

    Note: Use name: "root" and password: "ehf37q2rf7h"
    So we all can connect to the DB

Import to IntelliJ

    In the Database tool window (to the right), click the Data Source Properties icon.
    In the Data Sources and Drivers dialog, click the Add icon and select MySQL.
    If necessary --> At the bottom of the data source settings area, click the Download missing driver files link.
    Specify database connection details (according to the picture in this directory).
    To ensure that the connection to the data source is successful, click Test Connection.

    --> You can now execute SQL-Queries directly in IntelliJ