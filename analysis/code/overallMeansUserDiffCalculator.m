upperLimit = 38477;
diffFile = 'diff.txt';
totalDiff = 0;
for i=1:upperLimit
	cd(strcat('C:\Documents and Settings\ferhat\Desktop\492\plots\userClasses\', int2str(i)));
	
	data = load(diffFile);
	totalDiff = totalDiff + data;
end

cd('C:\Documents and Settings\ferhat\Desktop\');
totalDiff = totalDiff / upperLimit;
save('overallMeansUserDiff.txt', 'totalDiff', '-ascii');

