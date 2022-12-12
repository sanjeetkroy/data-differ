# Data Differ Tool

A Data Differ Tool which show the difference in nice looking HTML format.

### Use Case

It is designed to compare 2 SQL Data source.
It will fire-query on both data sources and prepare a report in html for each table.

### TODO Before running

1. Update [application.properties](/src/main/resources/application.properties) with your db details.
2. Update [TableType](/src/main/java/com/differ/enums/TableType.java) according to with your tables.
3. Update [Queries](/src/main/java/com/differ/constants/Queries.java) according to above tables.

### How to Run

This is a normal Sprint-Boot project. You just need to start the Application.

1. Go To StartApplication and hit start in any of your favourite IDE.
2. Once the Application is Up, hit [Generate API](http://localhost:8081/api/generate). 
3. If you have not updated [result.base.path], your report will be available under [Result Dir](/output/result)
4. To View Result, go to terminal in [Result Dir](/output/) and run `flask run`
   #### pre-requisites 
   You must have flask and Flask-AutoIndex, If not Please install using 
   1. `pip install Flask` [link](https://flask.palletsprojects.com/en/2.2.x/installation/)
   2. `pip install Flask-AutoIndex`[link](https://pypi.org/project/Flask-AutoIndex/)
5. Go to [Differ UI](http://127.0.0.1:5000/) 


### Sample Data Differ Output
[Sample Output](/output/result/sample.png). 