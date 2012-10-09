for i=1:101
data = load(strcat('C:\Documents and Settings\ferhat\IdeaProjects\CmpE491\userTimelinesData\userTimeline', int2str(i), '.txt'));
number = data(1:4:end);
day = data(2:4:end);
month = data(3:4:end);
year = data(4:4:end);
date = datenum(year, month, day);
plot(date, number, 's:');
datetick('x', 'yy mm dd');
xlabel('Date');
ylabel('Number of the resources that user added into his list');
title('Favorite List Size Timeline of the User');
saveas(gcf, strcat('userTimeline', int2str(i), '.jpeg'));
end