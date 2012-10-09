timeFavTimeline = 'TimeFavTimeline.txt';

userInterTime = [];
resourceInterTime = [];

for i=1:38477
	cd(strcat('C:\Documents and Settings\ferhat\Desktop\492\plots\userClasses\', int2str(i)));
	
	data = load(timeFavTimeline);
	diff = [];
	for j=1:length(data)
		if j == 1
			diff = [diff, data(1)];
		else 
			diff = [diff, data(j) - data(j-1)];
		end
	end
	
	userInterTime = [userInterTime, mean(diff)];
	
end

cd('C:\Documents and Settings\ferhat\Desktop');
save('userInterTime.txt', 'userInterTime', '-ascii');

for i=1:22809
	cd(strcat('C:\Documents and Settings\ferhat\Desktop\492\plots\resourceClasses\', int2str(i)));
	
	data = load(timeFavTimeline);
	diff = [];
	for j=1:length(data)
		if j == 1
			diff = [diff, data(1)];
		else 
			diff = [diff, data(j) - data(j-1)];
		end
	end
	
	resourceInterTime = [resourceInterTime, mean(diff)];
	
end

cd('C:\Documents and Settings\ferhat\Desktop');
save('resourceInterTime.txt', 'resourceInterTime', '-ascii');