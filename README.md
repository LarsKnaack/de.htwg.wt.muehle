[![Build Status](https://travis-ci.org/LarsKnaack/de.htwg.wt.muehle.svg?branch=master)](https://travis-ci.org/LarsKnaack/de.htwg.wt.muehle)

# Risiko

### 1. Purpose of the project
* Continuing the project [de.htwg.se.muehle](github.com/LarsKnaack/de.htwg.se.muehle)
* Setting up a WebUI with the Play!Framework for the lecture "Web technologies" at HTWG Konstanz

### 2. The client, the customer and other stakeholders
* Free to use
* Prof. Dr. Marco Boger

### 3. Users of the product
* Free to use for everyone

### 4. Mandated Constraints
* Requires scala and the Play!Framework
* Tested on Windows, but should run on every system

### 5. Naming Conventions and Definitions
* Classes end with packageName

### 6. Relevant Facts and Assumptions
* -
 
### 7. The Scope of the Work
* Presentation and documentation of the product

### 8. The Scope of the Product
* Multiplayer
* Communication between client and server via websockets

### 9. Functional and Data Requirements
* -
 
### 10. Look and Feel Requirements
* - 

### 11. Usability and Humanity Requirements
* WebUI to play the game
* Login mechanism via Google API or OpenID Mockup

### 12. Performance Requirements
* -

### 13. Operational and Environmental Requirements
* -

### 14. Maintainability and Support Requirements
* Test on other OS

### 15. Security Requirements
* -

### 16. Cultural and Political Requirements
* -

### 17. Legal Requirements
* License

### 18. Open Issues
* -

### 19. Off-the-Shelf Solutions
* -

### 20. New Problems
* -

### 21. Tasks
* Continuing the project in the lecture "Software Architectures" at HTWG Konstanz

### 22. Migration to the New Product
* -

### 23. Risks
* -

### 24. Costs
* -

### 25. User Documentation and Training
* -

### 26. Waiting Room
* -

### Structure of the game
* app
    * assets/main.less
        * Contains all styling
    * controllers
        * AuthenticationController
            * Contains all routes linked with authentication
        * HomeController
            * Contains all routes referenced by the Bootstrap Navbar
            * Contains utility methods, like websockets or javascript routing
    * models/User
        * Simple representation of an user
    * services
        * AuthenticatorService
            * Simple Authenticator for the play!security API
        * MuehleService
            * Interface to the underlying morris game
            * Routes calls to the controller of the game
        * UserService
            * Contains methods to authenticate, create or delete Users
        * WebsocketService
            * Contains methods to handle messages sent to the server
    * views
        * gui
            * Contains chat, log and a gamefield to play the game
        * index
            * Contains a theme chooser
        * login
            * Login via OpenID mock or via Google Sign-In
        * main
            * Main page, renders content of all other pages into this page
            * Contains Bootstrap navbar element
        * rules
            * Simple rule page
        * signup
            * Signup via OpenID mock
        * websocket
            * Creates a websocket in JS and handles messages on client's side
* conf
    * application.conf
        * Play! sample config file
    * routes
        * Contains all routes, which are available for the website
        * Contains utility routes, such as javascript routes or asset routes
* public
    * Contains files which are publicly available in the app, like pictures and stylesheets
    * javascripts/index
        * Creates theme chooser on the index page
    * polymer
        * Contains all polymer elements
        * src
            * login-app
                * Contains Google login button with all required scripts
            * morris-app
                * Contains Polymer button used in GUI
* .travis.yml
    * Travis build file, deployment to heroku
