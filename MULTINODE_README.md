# MultiNode Cluster

This is a scheduler implementation based on Spring scheduling which executes jobs only on one node in a cluster environment.
Each node tries to register a scheduler; the first one wins, the rest ones are disabled.
Once the node - which successfully registered the scheduler - is down, the other node replaces it.

Advantages:

* It uses only one table to synchronize nodes,
* works with a standard Spring @Scheduled annotation.

Disadvantages:

* It does not spread jobs execution among all nodes; all jobs are executed on one node.


Requisite :

* In application.properties place a default scheduler name i.e.
	#Multi-node scheduling
	scheduler.name=node1

	-> It will be the default node of execution of @Scheduled operation.

* Suppose we have two servers on our production environment then on second server's catalina.sh define scheduler.name via JAVA_OPTS
	
	path to catalina.sh : /bin/catalina.sh

	Parameter to be added : JAVA_OPTS="$JAVA_OPTS -Dscheduler.name=node2"
	
	This Parameter will be added after this statement : JAVA_OPTS="$JAVA_OPTS -Djava.protocol.handler.pkgs=org.apache.catalina.webresources"

* Add a table registered_scheduler for maintaining the status of current master node.
	
	CREATE TABLE `registered_scheduler` (
	  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
	  `scheduler_name` varchar(45) DEFAULT NULL,
	  `scheduler_type` varchar(45) DEFAULT NULL,
	  `heart_beat` timestamp NULL DEFAULT NULL,
	  PRIMARY KEY (`id`)
	) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;


Execution :

	If both the Tomcats are running then it will register only one server as master node and all the scheduled processes 	will run on that.

	->	Suppose tomcat_1 is the master node.
	-> 	Shut down the tomcat_1.
	->  After ~90 seconds node2 (tomcat_2) will executes jobs.

	It will automatically register the tomcat_2 as the master node on the execution of next @Scheduled operation as soon as node1 goes down.

