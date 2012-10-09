realTimeline = 'realTimeline.txt';
scaledTimeline = 'scaledTimeline.txt';
timeFavTimeline = 'TimeFavTimeline.txt';
for i=1:38477
	cd(strcat('C:\Documents and Settings\ferhat\Desktop\492\plots\resourceClasses\', int2str(i)));
	data = load(realTimeline);
	number = data(1:4:end);
	year = data(2:4:end);
	month = data(3:4:end);
	day = data(4:4:end);
	date = datenum(year, month, day);
	plot(date, number, 's:');
	datetick('x', 'yy mm dd');
	xlabel('Date');
	ylabel('Number of the users that added this resource into their list');
	title('Favorite List Size Timeline of the Resource');
	saveas(gcf, 'realTimeline.jpeg');
	
	cumamount = [];
	data = load(scaledTimeline);
	time = data(1:2:end);
	amount = data(2:2:end);
	for j=1:length(amount)
		if j == 1
			cumamount = [cumamount, amount(1)];
		else
			cumamount = [cumamount, cumamount(j-1) + amount(j)];
		end
	end
	plot(time, amount, 's:');
	xlabel('Time Percentage');
	ylabel('Favorites Percentage');
	title('Favorites vs Time Change Timeline of the Resource');
	saveas(gcf, 'scaledTimeline.jpg');
	
	plot(time, cumamount, 's:');
	xlabel('Time Percentage');
	ylabel('Cumulative Favorites Percentage');
	title('Favorites vs Time Change Timeline of the Resource');
	saveas(gcf, 'cummulativeScaledTimeline.jpg');
	meanCum = mean(cumamount);
	save('mean.txt', 'meanCum', '-ascii');
	
	data = load(timeFavTimeline);
	diff = [];
	for j=1:length(data)
		if j == 1
			diff = [diff, data(1)];
		else 
			diff = [diff, data(j) - data(j-1)];
		end
	end
	
	plot(diff, 's:');
	xlabel('Favorite Addition Times');
	ylabel('Number of the days that has passed from previous addition');
	title('The Distribution of the Days Between Being Favorite');
	saveas(gcf, 'betweenFavoritesTimeline.jpg');
end