upperLimit = 38477;
scaledTimeline = 'scaledTimeline.txt';
totalDiff = 0;
for i=1:upperLimit
	cd(strcat('C:\Documents and Settings\ferhat\Desktop\492\plots\userClasses\', int2str(i)));
	
	data = load(scaledTimeline);
	time = data(1:2:end);
	amount = data(2:2:end);
	
	diff = 0;
	for j=1:length(amount)
		diff = diff + time(j) - amount(j);
	end
	
	save('diff.txt', 'diff', '-ascii');
	
	totalDiff = totalDiff + diff;
end

cd('C:\Documents and Settings\ferhat\Desktop\');
totalDiff = totalDiff / upperLimit;
save('overallMeansUserDiff.txt', 'totalDiff', '-ascii');

upperLimit = 22809;
totalDiff = 0;
for i=1:upperLimit
	cd(strcat('C:\Documents and Settings\ferhat\Desktop\492\plots\resourceClasses\', int2str(i)));
	
	data = load(scaledTimeline);
	time = data(1:2:end);
	amount = data(2:2:end);
	
	diff = 0;
	for j=1:length(amount)
		diff = diff + time(j) - amount(j);
	end
	
	save('diff.txt', 'diff', '-ascii');
	
	totalDiff = totalDiff + diff;
end

cd('C:\Documents and Settings\ferhat\Desktop\');
totalDiff = totalDiff / upperLimit;
save('overallMeansResourceDiff.txt', 'totalDiff', '-ascii');