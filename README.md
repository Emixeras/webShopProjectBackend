# webshop project


# lokal

Docker image von Dockerhub ziehen:

    docker-compose pull 

Docker starten:
    
    docker-compose up


Docker stoppen:
    
    docker-compose down 


Datenbank zurücksetzen:

    docker-compose down --rmi all


Docker im Hintergrund starten und laufen lassen:

    docker-compose up -d
    
   
 # Backend Test:
 
 ## Hello World
    curl -i -X GET http://localhost:8080/api/HelloWorld/get
 
    {"value":"Dies ist der Hello World Test um 2020-10-23 17:42:28"}
    
# Dokumentation

entweder die Dokumentation über das Intelij Plugin aufrugen unter /ApiDocs

oder über das laufende Backend eine automatisch generierte Konfiguration: 
        http://localhost:8080/swagger-ui/#/

Vorteil der manuellen ist die integrierte Basic Auth Funktion, die automatisch generierte ist aber natürlich aktueller  