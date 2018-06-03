INSERT INTO `votingsystem`.`election`
(`electionid`,`topic`,`title`,`isVotable`)
VALUES
(0001,'Cars','Select most beautiful car',1);
INSERT INTO `votingsystem`.`election`
(`electionid`,`topic`,`title`,`isVotable`)
VALUES
(0002,'OSs','Select best OS',1);
INSERT INTO `votingsystem`.`election`
(`electionid`,`topic`,`title`,`isVotable`)
VALUES
(0003,'Smart Phones','Select most useful smart phone',1);




INSERT INTO `votingsystem`.`candidates`
(
`fname`,`surname`,`identityNumber`,`pword`,`street`,`dnumber`,`town`,`city`,`description`,`imagepath`,`electionid`,`votecounter`)
VALUES
(
'Enzo','Ferrari',1000,'Ferrari','Via Dino Ferrari','43–41053','Maranello','Italy','Ferrari Cars',
'C:\\Users\\yasin\\Downloads\\javafx\\first\\src\\main\\resources\\Pictures\\Candidates\\Cars\\ferrari-emblem.jpg',
0001,0);
INSERT INTO `votingsystem`.`candidates`
(
`fname`,`surname`,`identityNumber`,`pword`,`street`,`dnumber`,`town`,`city`,`description`,`imagepath`,`electionid`,`votecounter`)
VALUES
(
'Feruccio','Lamborghini',1001,'Lamborghini','Via Modena','12 40019','Sant’Agata Bolognese (Bologna)','Italy','Lamborghini Cars',
'C:\\Users\\yasin\\Downloads\\javafx\\first\\src\\main\\resources\\Pictures\\Candidates\\Cars\\Lamborghini-logo-1920x1080.png',
0001,0);
INSERT INTO `votingsystem`.`candidates`
(
`fname`,`surname`,`identityNumber`,`pword`,`street`,`dnumber`,`town`,`city`,`description`,`imagepath`,`electionid`,`votecounter`)
VALUES
(
'Tesla','Motors',1002,'Motors','Fremont Boulevard','45500','CA','USA','Tesla Cars',
'C:\\Users\\yasin\\Downloads\\javafx\\first\\src\\main\\resources\\Pictures\\Candidates\\Cars\\2000px-Tesla_Motors.svg.png',
0001,0);
INSERT INTO `votingsystem`.`candidates`
(
`fname`,`surname`,`identityNumber`,`pword`,`street`,`dnumber`,`town`,`city`,`description`,`imagepath`,`electionid`,`votecounter`)
VALUES
(
'Alfieri','Maserati',1003,'Maserati','Via Ciro Menotti','322','Modena','Italy','Maserati Cars',
'C:\\Users\\yasin\\Downloads\\javafx\\first\\src\\main\\resources\\Pictures\\Candidates\\Cars\\Maserati-logo.png',
0001,0);
INSERT INTO `votingsystem`.`candidates`
(
`fname`,`surname`,`identityNumber`,`pword`,`street`,`dnumber`,`town`,`city`,`description`,`imagepath`,`electionid`,`votecounter`)
VALUES
(
'Mercedes','Benz',1004,'Benz','Daimler AG','137 70327 ','Stuttgart','Germany','Mercedes-Benz Cars',
'C:\\Users\\yasin\\Downloads\\javafx\\first\\src\\main\\resources\\Pictures\\Candidates\\Cars\\Mercedes-Benz-logo-2011-1920x1080.png',
0001,0);


INSERT INTO `votingsystem`.`candidates`
(
`fname`,`surname`,`identityNumber`,`pword`,`street`,`dnumber`,`town`,`city`,`description`,`imagepath`,`electionid`,`votecounter`)
VALUES
(
'Microsoft','Windows',2000,'Windows','Levent Mah, Bellevue','No:7, 34340','Istanbul','Turkiye','Windows Operating System',
'C:\\Users\\yasin\\Downloads\\javafx\\first\\src\\main\\resources\\Pictures\\Candidates\\OSs\\windows.png',
0002,0);
INSERT INTO `votingsystem`.`candidates`
(
`fname`,`surname`,`identityNumber`,`pword`,`street`,`dnumber`,`town`,`city`,`description`,`imagepath`,`electionid`,`votecounter`)
VALUES
(
'Mac','OS',2001,'OS','Büyükdere Caddesi Levent','199 Kat:22-23 34394','Istanbul','Turkiye','Mac Operating System',
'C:\\Users\\yasin\\Downloads\\javafx\\first\\src\\main\\resources\\Pictures\\Candidates\\OSs\\macos.png',
0002,0);
INSERT INTO `votingsystem`.`candidates`
(
`fname`,`surname`,`identityNumber`,`pword`,`street`,`dnumber`,`town`,`city`,`description`,`imagepath`,`electionid`,`votecounter`)
VALUES
(
'Linux','GNU',2002,'GNU','Letterman Drive Building','D4700 94129','San Francisco','USA','Linux Operating System',
'C:\\Users\\yasin\\Downloads\\javafx\\first\\src\\main\\resources\\Pictures\\Candidates\\OSs\\linux-logo.jpg',
0002,0);


INSERT INTO `votingsystem`.`candidates`
(
`fname`,`surname`,`identityNumber`,`pword`,`street`,`dnumber`,`town`,`city`,`description`,`imagepath`,`electionid`,`votecounter`)
VALUES
(
'Apple','IPhone',3000,'IPhone','Büyükdere Caddesi Levent','199 Kat:22-23 34394','Istanbul','Turkiye','IPhone',
'C:\\Users\\yasin\\Downloads\\javafx\\first\\src\\main\\resources\\Pictures\\Candidates\\SmartPhones\\2000px-Apple_logo_black.svg.png',
0003,0);
INSERT INTO `votingsystem`.`candidates`
(
`fname`,`surname`,`identityNumber`,`pword`,`street`,`dnumber`,`town`,`city`,`description`,`imagepath`,`electionid`,`votecounter`)
VALUES
(
'Samsung','SmartPhone',3001,'SmartPhone','SAKARYA CD. ','NO:1/8','Ankara','Turkey','Samsung Smartphone',
'C:\\Users\\yasin\\Downloads\\javafx\\first\\src\\main\\resources\\Pictures\\Candidates\\SmartPhones\\2000px-Samsung_Logo.svg.png',
0003,0);
INSERT INTO `votingsystem`.`candidates`
(
`fname`,`surname`,`identityNumber`,`pword`,`street`,`dnumber`,`town`,`city`,`description`,`imagepath`,`electionid`,`votecounter`)
VALUES
(
'Sony','SmartPhone',3002,'SmartPhone','MS Mah. Selmanipak Cad.','46/B, 34672','Istanbul','Turkey','Samsung Smartphone',
'C:\\Users\\yasin\\Downloads\\javafx\\first\\src\\main\\resources\\Pictures\\Candidates\\SmartPhones\\Sony_logo.png',
0003,0);
