package models

object CodeGen extends App{

    slick.codegen.SourceCodeGenerator.run(
    
    "slick.jdbc.MySQLProfile",
    "org.mariadb.jdbc.Driver",
    "jdbc:mysql://localhost:3306/playwithscala",
    "/home/eren/bigdata/cases/playwithscala/app/",
    "models",Option("root"),Option("root"),true,false
    )

}