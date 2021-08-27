# CNSC-Research-Repository
The backend system of the CNSC Research Repository
## File Structure
``` 
com.cnsc.research   
                  |
                  | - api
                  | - configuration 
                  |                 |
                  |                 | - security
                  |                 | - util
                  |
                  | - domain        
                  |         |
                  |         | - exception
                  |         | - mapper
                  |         | - model
                  |         | - repository
                  |         | - transaction
                  |
                  | - service
```

- **api** - a package for controllers
- **configuration** - a package for pure java system configuration.
    - **security** - sub-package for security configurations.
    - **util** - package for utility configuration file.
- **domain** - a package for data.
    - **exception** - a package for custome exceptions.
    - **mapper** - mappers for model to transaction
    - **model** - package for POJO for JPA.
    - **repository** - data processing.
    - **transaction** - DTO package.
- **service** - a package for business logic.


