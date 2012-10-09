upperLimit = 38477;
scaledTimeline = 'scaledTimeline.txt';
corrFile = 'corr.txt';
totalCorr = [];
for i=1:upperLimit
	cd(strcat('C:\Documents and Settings\ferhat\Desktop\492\plots\userClasses\', int2str(i)));
	
	data = load(scaledTimeline);
	time = data(1:2:end);
	amount = data(2:2:end);
	
	c = corr(time, amount);
	save('corr.txt', 'c', '-ascii');
	
	totalCorr = [totalCorr, c];
end

cd('C:\Documents and Settings\ferhat\Desktop\');
save('overallTotalCorrUser.txt', 'totalCorr', '-ascii');

upperLimit = 22809;
totalCorr = [];
for i=1:upperLimit
	cd(strcat('C:\Documents and Settings\ferhat\Desktop\492\plots\resourceClasses\', int2str(i)));
	
	data = load(scaledTimeline);
	time = data(1:2:end);
	amount = data(2:2:end);
	
	c = corr(time, amount);
	save('corr.txt', 'c', '-ascii');
	
	totalCorr = [totalCorr, c];
end

cd('C:\Documents and Settings\ferhat\Desktop\');
save('overallTotalCorrResource.txt', 'totalCorr', '-ascii');