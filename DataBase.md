# Introduction #
Dans le cadre du projet ParkMe, une DataBase MySQL est mise en place afin de stocker les informations propres aux utilisateurs et aux places de parking proposées.

# Details #

Les tables sont décrites ainsi :
  * | Nom | Type |
|:----|:-----|
## user ##
  1. | PK:id | INT|
  1. | Unique:mail | VARCHAR |
  1. | crypted\_pwd (ou token) | VARCHAR |

## vehicle ##
  1. | PK:id | INT |
|:------|:----|
  1. | type | VARCHAR |
  1. | photo | VARCHAR |
  1. | FK\_user:user\_id | INT |
  1. | description | VARCHAR |

## parking\_spot ##
  1. | PK:id | INT |
|:------|:----|
  1. | lat | FLOAT |
  1. | lng | FLOAT |
  1. | FK\_user:user\_id | INT |
  1. | time\_when\_ready | TIMEDATE |
  1. ...