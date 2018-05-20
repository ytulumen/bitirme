use votingsystem;
CREATE Table electioncandidate(
	id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	electionid INT(6) UNSIGNED NOT NULL,
    candidateid INT(6) UNSIGNED NOT NULL,
    voteNumber INT(6) UNSIGNED
);



use votingsystem;
CREATE Table electionvoter(
	id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	electionid INT(6) UNSIGNED NOT NULL,
    voterid INT(6) UNSIGNED NOT NULL
);

use votingsystem;
CREATE Table voters(
	id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	fname VARCHAR(30) NOT NULL,
	surname VARCHAR(30) NOT NULL,
	identityNumber INT(11) UNSIGNED,
	pword VARCHAR(30) NOT NULL,
	street VARCHAR(30),
	dnumber VARCHAR(30),
	town VARCHAR(30),
	city VARCHAR(30),
	fprint INT(6) UNSIGNED
);


use votingsystem;
CREATE Table candidates(
	id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	fname VARCHAR(30) NOT NULL,
	surname VARCHAR(30) NOT NULL,
	identityNumber INT(11) UNSIGNED,
	pword VARCHAR(30) NOT NULL,
	street VARCHAR(30),
	dnumber VARCHAR(30),
	town VARCHAR(30),
	city VARCHAR(30),
	description VARCHAR(500) NOT NULL,
    imagepath VARCHAR(256) NOT NULL,
    electionid INT(6) NOT NULL,
    votecounter INT(6) NOT NULL
);

use votingsystem;
CREATE Table election(
	id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	electionid INT(6) UNSIGNED ,
	topic VARCHAR(500) NOT NULL,
    title VARCHAR(256) NOT NULL
)