Part A
	In this part, the database file has been generated to classify three different activities: walking, running, and jumping. Each activity duration is 5 seconds and the data sampling frequency is over 10 Hz for the proper accuracy. Also, the number of each activity in the total dataset is  20. Thus, the total dataset matrix size is 150 (X, Y, Z axes with 5 sec and 10Hz) × 60 (3 activities with 20 times). According to recent research papers, accelerometer sensor datasets (X, Y, and Z axes) are very useful for human activity recognition, so we have used  accelerometer sensors as input dataset among many sensors in the smartphone. The below table is an example of database scheme. 

ID
(Key)	Accel X 1st	Accel Y 1st	Accel Z 1st	Accel X 2nd	……….	Accel X 50th	Accel Y 50th	Accel Z 50th	Activity Label
※When submitting assignment, you must attach a sample DB file which you create by Part A.

Part B
	Based on the database which was generated (Part A), our application classifies the activities using Support Vector Machine.  The accuracy is over 80%. For the validated test accuracy, ‘K-fold cross-validation technique’ has been used. The ‘K’ should be between three and five. 
