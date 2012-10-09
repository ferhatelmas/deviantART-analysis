for j=1:22809
	if j == 10267
		continue;
	end
	
	cd(strcat('C:\Users\Ginger\Desktop\finalProject\real\resourceClasses\', int2str(j)));
	
	if exist('interarrivals') == 7
		delete('interarrivals');
	end
	
	if exist('interarrivals.txt') == 7
		delete('interarrivals.txt');
	end
	
	
	if exist('interarrivals2.txt') == 7
		delete('interarrivals.txt');
	end
	
	data = load('scaledTimeline.txt');
	time = data(1:2:end);
	amount = data(2:2:end);
	
	add = [0 0 0 0 0 0 0 0 0 0 0 0];
	cnt = [0 0 0 0 0 0 0 0 0 0 0 0];
	for i=1:length(time)
		if time(i) <= 0.05
			add(1) = add(1) + time(i) - amount(i);
			cnt(1) = cnt(1) + 1;
		elseif time(i) <= 0.1
			add(2) = add(2) + time(i) - amount(i);
			cnt(2) = cnt(2) + 1;
		elseif time(i) <= 0.15
			add(3) = add(3) + time(i) - amount(i);
			cnt(3) = cnt(3) + 1;
		elseif time(i) <= 0.2
			add(4) = add(4) + time(i) - amount(i);
			cnt(4) = cnt(4) + 1;
		elseif time(i) <= 0.25
			add(5) = add(5) + time(i) - amount(i);
			cnt(5) = cnt(5) + 1;
		elseif time(i) <= 0.3
			add(6) = add(6) + time(i) - amount(i);
			cnt(6) = cnt(6) + 1;
		elseif time(i) <= 0.35
			add(7) = add(7) + time(i) - amount(i);
			cnt(7) = cnt(7) + 1;
		elseif time(i) <= 0.4
			add(8) = add(8) + time(i) - amount(i);
			cnt(8) = cnt(8) + 1;
		elseif time(i) <= 0.45
			add(9) = add(9) + time(i) - amount(i);
			cnt(9) = cnt(9) + 1;
		elseif time(i) <= 0.5
			add(10) = add(10) + time(i) - amount(i);
			cnt(10) = cnt(10) + 1;
		elseif time(i) <= 0.5
			add(11) = add(11) + time(i) - amount(i);
			cnt(11) = cnt(11) + 1;
		else
			add(12) = add(12) + time(i) - amount(i);
			cnt(12) = cnt(12) + 1;
		end
	end

	first = add(1) / cnt(1);
	second = sum(add(1:2)) / sum(cnt(1:2));
	third = sum(add(1:3)) / sum(cnt(1:3));
	fourth = sum(add(1:4)) / sum(cnt(1:4));
	fifth = sum(add(1:5)) / sum(cnt(1:5));
	sixth = sum(add(1:6)) / sum(cnt(1:6));
	seventh = sum(add(1:7)) / sum(cnt(1:7));
	eighth = sum(add(1:8)) / sum(cnt(1:8));
	nineth = sum(add(1:9)) / sum(cnt(1:9));
	tenth = sum(add(1:10)) / sum(cnt(1:10));
	seventyfive = sum(add(1:11)) / sum(cnt(1:11));
	hundred = sum(add(1:12)) / sum(cnt(1:12));
	
	save('interarrivals.txt', 'first', 'second', 'third', 'fourth', 'fifth', 'sixth', 'seventh', 'eighth', 'nineth', 'tenth', 'seventyfive', 'hundred', '-ascii');
		
end
