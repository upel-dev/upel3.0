# Deployment
## Access
- The main app link - https://upel3.herokuapp.com/  
- The github link - https://git.heroku.com/upel3.git

## Updates
To deploy the new app version build the project to jar (`./gradlew build`) 
and then run the following command from the project root folder:  
`heroku deploy:jar build/libs/upel3-0.0.1-SNAPSHOT.jar --app upel3`

## Debugging
`heroku logs --tail  --app upel3`
