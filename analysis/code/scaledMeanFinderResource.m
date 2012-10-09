upperLimit = 22809;
scaledTimeline = 'scaledTimeline.txt';
timeFavTimeline = 'timeFavTimeline.txt';
delFile = 'cummulativeScaledTimeline.jpg';
totalScaled = 0;
totalFav = 0;
for i=1:upperLimit
	cd(strcat('C:\Documents and Settings\ferhat\Desktop\492\plots\resourceClasses\', int2str(i)));
	delete(delFile);
	data = load(scaledTimeline);
	time = data(1:2:end);
	amount = data(2:2:end);
	
	m1 = mean(amount);
	totalScaled = totalScaled + m1;
	
	data = load(timeFavTimeline);
	diff = [];
	for j=1:length(data)
		if j == 1
			diff = [diff, data(1)];
		else 
			diff = [diff, data(j) - data(j-1)];
		end
	end
	
	m2 = mean(diff);
	save('mean.txt', 'm1', 'm2', '-ascii');
	
	totalFav = totalFav + m2;
end

cd('C:\Documents and Settings\ferhat\Desktop\');
totalScaled = totalScaled / upperLimit;
totalFav = totalFav / upperLimit;
save('overallMeansResource.txt', 'totalScaled', 'totalFav', '-ascii');