node {
	
	docker.image("node:4.2.2").inside {
		sh """
			npm install -g bower gulp
			npm install 
			bower install 
			gulp build
		"""	
	}

 	docker.image("maven:3-jdk-8").inside('-v /var/maven:/root/.m2') {
        sh "mvn package"
 	}

 	docker.build("breizchamp/cfp:latest").push()
}

