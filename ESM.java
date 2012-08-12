package ESMAssign;

import java.io.*;

import java.text.*;
import java.util.*;



public class ESM {
	
	//Products List before sell
	ArrayList<Product> orignial = new ArrayList<Product>();
	//Products List
	ArrayList<Product> productList = new ArrayList<Product>();
	//Sell List
	ArrayList<Sell> outS = new ArrayList<Sell>();
	//Discard List
	ArrayList<Product> outD = new ArrayList<Product>();
	//Available List
	ArrayList<Product> avai = new ArrayList<Product>();
	//Output stock List
	ArrayList<Product> stock = new ArrayList<Product>();
	
	ArrayList<Product> finalStock = new ArrayList<Product>();
	
	//Output report List
	ArrayList<Product> report = new ArrayList<Product>();
	
	double profit = 0;
	double lose = 0;
	int i = 0;
	int op = 0;
	int [] flags = new int [1000];
	
	public ArrayList<Product> readProduct(String fileName) {
		
		String s;
		try {
			File file = new File(fileName);
			Scanner scanner;
			scanner = new Scanner(file);

			op = 0;
			
			while (scanner.hasNextLine()) {
				Product product = new Product();
				s = scanner.nextLine();
				boolean flag = true;
				if(s.isEmpty())flag = false;
				while (!s.trim().equals("")) {
//					System.out.println(s);
					String[] s1 = s.split("\\s+", 2);
					String[] s2 = s.split("\\s+", 3);
					
					DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy"); sdf.setLenient(false);
					if (s1[0].equalsIgnoreCase("product")) {
						product.setName(s1[1].trim());
						//flags[op]++;

					} else if (s2[0].equalsIgnoreCase("quantity")) {
						try{
							//System.out.println(s2[1].trim());
						product.setInQuantity(Integer.parseInt(s2[1].trim()));
						}
						catch(Exception e){
							flag = false;
						}
						//flags[op]++;

					} else if (s2[0].equalsIgnoreCase("boughtAt")) {
						String boughtAt = s2[1].trim().substring(1);
						try {
							product.setBoughtAt(Double.parseDouble(boughtAt));
							//flags[op]++;
							if (Double.parseDouble(boughtAt) < 0) {
								flag = false;
							}
						} catch (Exception e) {
							flag = false;
//							System.out.println(e);
						}

					} else if (s2[0].equalsIgnoreCase("boughtOn")) {
						try {
							product.setBoughtOn(sdf.parse(s2[1].trim()));
							//flags[op]++;

						} catch (ParseException e) {
							flag = false;
//							System.out.println(e);
						}
					} else if (s2[0].equalsIgnoreCase("useBy")) {
						try {
							product.setUseBy(sdf.parse(s2[1].trim()));
							//flags[op]++;

						} catch (ParseException e) {
							flag = false;
//							System.out.println(e);
						}
					}
					if (scanner.hasNextLine()) {
						s = scanner.nextLine();
					} else {
						break;
					}
				}
//				System.out.println(product.getName());
				try{
					if(product.getBoughtAt() <= 0 || product.getName().equals(null) || product.getBoughtOn()==null || product.getInQuantity() <= 0 ){
						flag =false;
					}
				}
				catch(Exception e){
					flag =false;
				}


				if(flags[op]>5)
					flag = false;
				DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy"); sdf.setLenient(false);
				try{
				if(product.getUseBy().equals(sdf.parse("1-1-1000"))){
					if(flags[op]>4)
						flag = false;
					}
				}catch(Exception e1) {}
					
				op++;

				
				if (flag) {
					productList.add(product);
//					System.out.println("1\n" + product.getName());
				}
			}
			scanner.close();
		} catch (FileNotFoundException e1) {
			 
//			System.out.println(e1);
		}


		

		return productList;


	}

	public void readInstruction(String fileName, ArrayList<Product> productList) {
		
		DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy"); sdf.setLenient(false);
		Date testR = null;
		try {
			testR = sdf.parse("31-12-2099");
		}
		catch (ParseException e) {
//			e.printStackTrace();
		}
		try {
			File file = new File(fileName);
			Scanner scanner;
			scanner = new Scanner(file);

			while (scanner.hasNextLine()) {
				String s = scanner.nextLine();
				String [] action = s.split("\\s+", 2);
				if (action[0].trim().equalsIgnoreCase("buy")) {
					productList = buy(action[1], productList);
				}
			}
			
		
			for(int i = 0; i < productList.size(); i++){
				Product prod = new Product();
				prod.setBoughtAt(productList.get(i).getBoughtAt());
				prod.setBoughtOn(productList.get(i).getBoughtOn());
				prod.setInQuantity(productList.get(i).getInQuantity());
				prod.setName(productList.get(i).getName());
				prod.setUseBy(productList.get(i).getUseBy());
				orignial.add(prod);
//				System.out.println("cast" + i);
			}	
			

			
			

			scanner = new Scanner(file);
			int f = 0;
			while (scanner.hasNextLine()) {
				String s = scanner.nextLine();
				String [] action = s.split("\\s+", 2);
				if (action[0].trim().equalsIgnoreCase("sell")) {
//					System.out.println("**********1********"+ productList.size() + orignial.size());

					if(f==0){
						stock = sell(productList, action[1],testR);
						f++;
					}
					else{
						stock = sell(stock, action[1],testR);
					}
				}
			}
			

			
//			for(Product p: orignial){
////				System.out.println("After~~***~~orignial" + p.getBoughtOn() + p.getBoughtAt() + p.getUseBy() + p.getName() + p.getInQuantity());
//			}
//			
//			for(Sell p: outS){
////				System.out.println("***" + p.getUseBy() + p.getName()  + "***" + p.getNetIncome() + "****" + p.getSoldAt()   
////						 + "***" + p.getBeforeQuantity() + "***" + p.getInQuantity()  + "***" + p.getOutQuantity());
//				
//			}

			scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String s = scanner.nextLine();
				String [] action = s.split("\\s+", 2);
				if (action[0].trim().equalsIgnoreCase("discard")) {
					finalStock = discard(action[1], stock);
				}
			}
			
			save();
			
			scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String s = scanner.nextLine();
				String [] action = s.split("\\s+", 2);

				if (action[0].trim().equalsIgnoreCase("query")) {

					String[] str2;;
					try{
						str2 = action[1].split("\\s+");
					}
					catch(Exception e){
						break;
					}
					if (str2[0].equalsIgnoreCase("profit")) {
						profit_query(str2[1].trim(), str2[2].trim());
					} else if (str2[0].equalsIgnoreCase("bestsales")) {
						bestSales(str2[1].trim(), str2[2].trim());
					} else if (str2[0].equalsIgnoreCase("worstsales")) {
						worstSales(str2[1].trim(), str2[2].trim());
					} 

				}
			}


			scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String s = scanner.nextLine();
				String [] action = s.split("\\s+", 2);

				if (action[0].trim().equalsIgnoreCase("query")) {
					report.clear();
					for(int i = 0; i < orignial.size(); i++){
						Product prod = new Product();
						prod.setBoughtAt(orignial.get(i).getBoughtAt());
						prod.setBoughtOn(orignial.get(i).getBoughtOn());
						prod.setInQuantity(orignial.get(i).getInQuantity());
						prod.setName(orignial.get(i).getName());
						prod.setUseBy(orignial.get(i).getUseBy());
						report.add(prod);
//						System.out.println("cast" + i);
					}	
					
					String[] str2;
					try{
						str2 = action[1].split("\\s+");
					}
					catch(Exception e){
						break;
					}
					if (str2[0].equalsIgnoreCase("profit")) {
//						profit_query(str2[1].trim(), str2[2].trim());
					} else if (str2[0].equalsIgnoreCase("bestsales")) {
//						bestSales(str2[1].trim(), str2[2].trim());
					} else if (str2[0].equalsIgnoreCase("worstsales")) {
//						worstSales(str2[1].trim(), str2[2].trim());
					} 	else{
						
						if(str2.length == 1){
//							System.out.println("!!!" + str2.length + "!!!"  + str2[0] );	

						
						Date newPoint = null;
						try{
							
							newPoint = sdf.parse(str2[0].trim());
							Scanner scannerF;
							scannerF = new Scanner(file);

							while (scannerF.hasNextLine()) {
								
								String s1 = scannerF.nextLine();
								String [] action1 = s1.split("\\s+", 2);
								if (action1[0].trim().equalsIgnoreCase("sell")) 
								{
									report = sell(report, action1[1],newPoint);
								}
							}
							
							stock_query(str2[0].trim(), report);	 
//							System.out.println("query !!" + orignial.size() + newPoint + report.size());
							 
						}
						catch(Exception e){
							System.out.println("Failed query " + str2[0].trim());
						}						}

				}
			}

			}
		} catch (FileNotFoundException e) {
//			System.out.println(e);
		}
	}

	public ArrayList<Product> buy(String action, ArrayList<Product> productList) {
		DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy"); sdf.setLenient(false);
		boolean flag = true;
		Product product = new Product();

		String[] details = action.split("; ");

		for (String str : details) {
			String[] str1 = str.split(" ", 2);

			if (str1[0].trim().equalsIgnoreCase("product")) {
				product.setName(str1[1].trim());
//				System.out.println(product.getName());
			} else if (str1[0].trim().equalsIgnoreCase("quantity")) {
				product.setInQuantity(Integer.parseInt(str1[1].trim()));
				if (Integer.parseInt(str1[1].trim()) < 0) {
					flag = false;
				} 
			} else if (str1[0].trim().equalsIgnoreCase("boughtOn")) {
				try {
					product.setBoughtOn(sdf.parse(str1[1].trim()));
				} 
				catch (ParseException e) {
					flag = false;
//					System.out.println(e);
				}
			} else if (str1[0].trim().equalsIgnoreCase("boughtAt")) {
				String boughtAt = str1[1].trim().substring(1);

				product.setBoughtAt(Double.parseDouble(boughtAt));

				if (Double.parseDouble(boughtAt) < 0) {
					flag = false;
				} 
			} else if (str1[0].trim().equalsIgnoreCase("useBy")) {
				try {
					product.setUseBy(sdf.parse(str1[1].trim()));
				} catch (ParseException e) {
					flag = false;
//					System.out.println(e);
				}
			}
		}
		try{
			if(product.getBoughtAt() <= 0 || product.getName().equals(null) || product.getBoughtOn()==null || product.getInQuantity() <= 0 ){
				flag =false;
			}
		}
		catch(Exception e){
			flag =false;
		}
		
		
		
		if(flag){
			productList.add(product);
		}
		
//		for(Product p: productList){
////			System.out.println(p.getUseBy() + p.getName());
//			
//		}
		
		//Products List before sell


		return productList;
		
		
	}
	
	public ArrayList<Product> sortD(ArrayList<Product> productList){
		
		int sizeP = productList.size();
		double cost = 0.0;
		double temp = 0.0;
		
		int key = 0;
		for (int y = 0; y < sizeP; y++){
			cost = 0;
			temp = 0;
			key = y;
			for(Product pp : productList){
				temp = pp.getInQuantity()*pp.getBoughtAt();
				if (temp > cost && productList.indexOf(pp)>=y){
					key = productList.indexOf(pp);
					cost = temp;
				}
			}
			Collections.swap(productList,y,key);
//			System.out.println(cost);
		}
		return productList;
	}
	
	
	public ArrayList<Product> sort(ArrayList<Product> productList){
		DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy"); sdf.setLenient(false);
		Date testD = null;
		try {
			testD = sdf.parse("01-01-1000");
		}
		catch (ParseException e) {
//			e.printStackTrace();
		}
		
		int sizeP = productList.size();
		
		Product Min = new Product();
		int key = 0;
		ArrayList<Product> stock = new ArrayList<Product>();
		
//		for(Product p: productList){
////			System.out.println(p.getUseBy() + p.getName());
//			
//		}
		
		//sort by use by date
		for(int p = 0; p <sizeP; p++){
			Product P = productList.get(p);
			Min = P;
			key = 0;
			for(int m = 1; m <sizeP; m++){
				Product M = productList.get(m);
				if(M.getUseBy().before(Min.getUseBy()) || M.getUseBy().equals(Min.getUseBy()) ){
					Min = M;
					key = m;		
				}
			}
			stock.add(Min);
			productList.remove(key);
			p = -1;
			sizeP--;
		}		
		
		sizeP = stock.size();

		//sort by price if same product with same useby date
		for(int p = 0; p <sizeP; p++){

			Product P = stock.get(p);
			Min = P;
			key = 0;
			for(int m = 1; m <sizeP; m++){

				Product M = stock.get(m);
				if(!M.getUseBy().equals(testD)){
					if(M.getBoughtAt()>(Min.getBoughtAt()) && M.getUseBy().equals(Min.getUseBy()) && M.getName().equals(Min.getName())){
						Collections.swap(stock, m, p);
					}
				}
			}
		}	
		
		
		sizeP = stock.size();
		//merge same items
		for(int p = 0; p <sizeP-1; p++){

			Product P = stock.get(p);
			Product M = stock.get(p+1);

			if(M.getInQuantity()==(P.getInQuantity()) &&M.getBoughtOn().equals(P.getBoughtOn()) &&M.getBoughtAt()==(P.getBoughtAt()) && M.getUseBy().equals(P.getUseBy()) && M.getName().equals(P.getName())){

					//System.out.println("in it");
					int half = stock.get(p).getInQuantity();
					stock.get(p+1).setInQuantity(2*half);
					stock.remove(p);
					p = -1;
					sizeP--;

			}
		}	
		
		
//		for(Product p: stock){
////			System.out.println(p.getUseBy());
//		}
		return stock;
	}
	
	
	public ArrayList<Product> sell(ArrayList<Product> list, String action, Date point) {
		

		
		String[] ins = (action.toLowerCase()).split("; ");
		
		Sell sold = new Sell();
		DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy"); sdf.setLenient(false);
		Date testD = null;
		try {
			testD = sdf.parse("01-01-1000");
		}
		catch (ParseException e) {
//			e.printStackTrace();
		}


		boolean flag = true;
		boolean quan = false;
		
//		FileOutputStream fs;
		String name = null;
		
		
		try {

			boolean sellFlag = false;
			
			if(sellFlag){			}
			for (String sell : ins) {
				
				String[] details = sell.split(" ", 2);
				
				if (details[0].trim().equalsIgnoreCase("product")) {
					sold.setName(details[1].trim());
					name = details[1].trim();

				} else if (details[0].trim().equalsIgnoreCase("soldOn")) {
					try {
						sold.setSoldOn(sdf.parse(details[1].trim()));
//						System.out.println(sold.getSoldOn());
					} catch (ParseException e) {
						sellFlag = false;
//						System.out.println(e);
					}
				} else if (details[0].trim().equalsIgnoreCase("soldAt")) {
					String soldAt = details[1].trim().substring(1);
					sold.setSoldAt(Double.parseDouble(soldAt));
					if (Double.parseDouble(soldAt) < 0) {
						flag = false;
						System.out
								.println("Wrong instruction:\n Sell " + action);
						System.out
								.println("Sell price must be no less than 0!!!");
//						out.append("Wrong instruction:\n Sell " + action + "\n");
//						out.append("Sell price must be no less than 0!!!\n");
//						out.append("\n");

					} 
				} else if (details[0].trim().equalsIgnoreCase("quantity")) {
					try{
					sold.setOutQuantity(Integer.parseInt(details[1].trim()));
					}
					catch(Exception e) {
						flag = false;
//						System.out.println(e);
					}
					int outQ = 0;
					try{
					outQ = Integer.parseInt(details[1].trim());
					}
					catch(Exception e) {
						flag = false;
//						System.out.println(e);
					}
					
					if (outQ < 0) {
						flag = false;
						System.out
								.println("Wrong instruction:\n Sell " + action);
						System.out
								.println("Quantity must be no less than 0!!!");
//						out.append("Wrong instruction:\n Sell " + action + "\n");
//						out.append("Quantity must be no less than 0!!!\n");
//						out.append("\n");
					} 
				}

			}
			

			try{
				
				if( !sold.getName().isEmpty() && sold.getOutQuantity()>0 && sold.getSoldAt()>0 && sold.getSoldOn()!=null)
				{
//					System.out.println("can sell");
					sellFlag = true;
					flag = true;
//					System.out.println(sold.getName());
				}	
			}
			catch(Exception e){
				flag = false;
			}

			
//			System.out.println("Sold-Start");
			
			
			if(sold.getSoldOn().after(point) || sold.getSoldOn().equals(point)){
				flag = false;
			}
			
//			if (flag) {
//				System.out.println("can sell");
//				System.out.println(sold.getName());
//			}else{
//				System.out.println("can not sell");
//				System.out.println(sold.getName());
//			}
			
			if (flag) {
				
				if (!list.isEmpty()) {
					int count = 0;
					int [] listO = new int [list.size()];
					int x = 0; 
//					System.out.println("******************");
					list = sort(list);
					
					for (Product p : list) {
						if (p.getName().equalsIgnoreCase(sold.name)) {
							//not expired && earn from trade
							if(p.getUseBy().after(sold.getSoldOn()) && p.getBoughtAt()<sold.getSoldAt()){
								count = count + p.getInQuantity();
								avai.add(p);
							}
														
							else if (p.getUseBy().equals(testD) && p.getBoughtAt()<sold.getSoldAt()){
								count = count + p.getInQuantity();
								avai.add(p);
							}
							
							else if(p.getUseBy().before(sold.getSoldOn())){
								count = count + p.getInQuantity();
								avai.add(p);
								listO [x] = list.indexOf(p);
								x++;
							}
							if (count >= sold.getOutQuantity()) 
								quan = true;
						}
					}
					
					//move the expired item to the end
					int indexTo = list.size() - 1;
					for (int l = 0; l < x; l++)
					Collections.swap(list, listO [l], indexTo);

					if (quan){
						outS.add(sold);
						sold.setBeforeQuantity(count);
						int outQuantity = sold.getOutQuantity();
						int i = 0;
						int[] set = new int[list.size()];
						profit = 0;
						int[] remove = new int[list.size()];
//						System.out.println(remove);
						Product[] sp = new Product[list.size()];
						for (Product pp : list) {
							
							
							i++;
							if (pp.getName().equalsIgnoreCase(sold.name)) {
//								if (pp.getUseBy().before(sold.getSoldOn()))
//									break;
								if (pp.getInQuantity() > sold.getOutQuantity()) {
									pp.setInQuantity(pp.getInQuantity()
											- sold.getOutQuantity());
									profit = profit + sold.getOutQuantity() *(sold.getSoldAt() - pp.getBoughtAt());
																		set[i - 1] = i;
									sp[i - 1] = pp;
									break;
								} else if (pp.getInQuantity() == sold.getOutQuantity()) {
									profit = profit + sold.getOutQuantity() *(sold.getSoldAt() - pp.getBoughtAt());
									remove[i - 1] = i;
																		break;
								} 
								else if (pp.getInQuantity() < sold.getOutQuantity()) {
									profit = profit + pp.getInQuantity() *(sold.getSoldAt() - pp.getBoughtAt());
									sold.setOutQuantity((sold.getOutQuantity() - pp.getInQuantity()));
									remove[i - 1] = i;
																	}
							}

						}
						
						sold.setOutQuantity(outQuantity);
						for (int j : set) {
							if (j > 0) {
								list.set(j - 1, sp[j - 1]);
							}

						}
						int m = 0;
						for (int k : remove) {
							if (k != 0) {
								list.remove(k - 1 - m);
								m++;
							}

						}
						sold.setNetIncome(profit);
					} 
					
					else if (count == 0) {
						System.out.println("No such product-" + name
								+ " in stock!!!");
//						out.append("No such product-" + name + " in stock!!!\n");
//						out.append("\n");
					} else {
						System.out.println("No enough such product-" + name
								+ " in stock!!!");
//						out.append("No enough such product-" + name
//								+ " in stock!!!\n");
//						out.append("\n");
					}
				} else {
					System.out.println("No such product-" + name
							+ " in stock!!!");
//					out.append("No such product-" + name + " in stock!!!\n");
//					out.append("\n");
				}
			}
			
			
			//Test part
			//Test part
			//Test part
			//Test part
			
			
			
//			System.out.println("size!!!!!!!!!!!!" + productList.size());
			
			

//			System.out.println(list.size());

			
		} catch (Exception e) {
			 
//			System.out.println(e);
		}
//		for(Product p: list){
////			System.out.println("After~~~~productList" + p.getBoughtOn() + p.getBoughtAt() + p.getUseBy() + p.getName() + p.getInQuantity());
//		}
		return list;

	}
	
	public ArrayList<Product> discard(String action, ArrayList<Product> products) {
		
		
		boolean dscd = false;
		
//		FileOutputStream fs;

		
		DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy"); sdf.setLenient(false);
		Date testD = null;
		Date discard = null;
		try {
			testD = sdf.parse("01-01-1000");
			discard = testD;
		}
		catch (ParseException e) {
//			e.printStackTrace();
		}
		boolean flag = true;
		boolean flagDiscard = false;
		Product product = new Product();
		
		String[] details = action.split(";\\s+");
		int x = 0;
		for (String str : details) {
			
			String[] str1 = str.split("\\s+", 2);
			String[] str2 = str.split("\\s+", 3);
			
			if (x == 0){
				try{
					discard = sdf.parse(str2[0].trim());
//					System.out.println("dsadsad dsa d sad sa " + discard);
					break;
				}
				catch(Exception e){
//					System.out.println(e);
				}
			}
			
			if (str1[0].trim().equalsIgnoreCase("product")) {
				product.setName(str1[1].trim());
//				System.out.println(product.getName());
			} else if (str1[0].trim().equalsIgnoreCase("quantity")) {
				product.setInQuantity(Integer.parseInt(str1[1].trim()));
//				System.out.println(product.getInQuantity());
				if (Integer.parseInt(str1[1].trim()) < 0) {
					flag = false;
				} 

			} else if (str1[0].trim().equalsIgnoreCase("useBy")) {
				try {
					product.setUseBy(sdf.parse(str1[1].trim()));
//					System.out.println(product.getUseBy());
				} catch (ParseException e) {
					flag = false;
//					System.out.println(e);
				}
			}
			x++;
		}
		
//		System.out.println(product.getName());
		try{
			if(!product.getName().isEmpty() && !product.getUseBy().equals(testD) && product.getInQuantity() > 0 ){
				flag =true;
			}

		}
		catch(Exception e){
			flag =false;

		}
		
		try{
			if(discard != testD){
				flagDiscard = true;
			}

		}
		catch(Exception e){

			flagDiscard = false;
		}
		

		
		
		int indexU []= new int [products.size()];
		int indexD = 0;
		

//			System.out.println("12-12" + flagDiscard);
//			System.out.println("quantity" + flag);
		int count = 0;

		if (flagDiscard) {
				dscd = false;

				
				if (!products.isEmpty()) {
					
					for (Product p : products) {
						
						if (p.getUseBy().before(discard) && !p.getUseBy().equals(testD)) {
							//expired
								dscd = true;
								indexU [products.indexOf(p)]= products.indexOf(p);
						}
						else{
							indexU [products.indexOf(p)] = -1;
						}

					}
					if (dscd){
						for(int r = 0; r<indexU.length; r++){
							if (indexU[r] != -1){
								outD.add(products.get(indexU[r]-count));
								products.remove(indexU[r]-count);
//								System.out.println("Discard-Start " + dscd + "      " + (indexU[r]-count));
								count++;
								
								}
							}
						}
					
				}
				
//				for(Product p: outD){
////					System.out.println("After!outD" + p.getUseBy() + p.getName() + p.getInQuantity());
//					
//				}
			}
			
			
			
			
			
		if (flag) {
			dscd = false;

			
			if (!products.isEmpty()) {
				
				for (Product p : products) {
					if (p.getName().equalsIgnoreCase(product.name)) {
						//not expired && earn from trade

						if(p.getUseBy().equals(product.getUseBy()) && p.getInQuantity()== product.getInQuantity()){
							dscd = true;
							indexD = products.indexOf(p);
							
						}
					}
				}
				
				if (dscd){
					outD.add(products.get(indexD));
					products.remove(indexD);
//					System.out.println("Discard-Start" + dscd + "      " + indexD);
					}
				else{
//					System.out.println("No such item to discard matches the Quantity, Useby date and Name!\n Failed: discard " + action );
					
				}
			}
			
//			for(Product p: products){
////				System.out.println("After!productList" + p.getUseBy() + p.getName() + p.getInQuantity());
//				
//			}
		}
		return products;
		
	}
	
	public void save() {
		
		DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		
		if(outD.size()==0)
			finalStock = stock;
		else if (outS.size()==0)
			finalStock = productList;
		
		
		
		try {
			File file = new File("output.txt");
			PrintWriter writer = new PrintWriter(file);
			writer.print("");
			writer.close();
		}
		catch(FileNotFoundException e){
//			System.out.println(e);
		}

		

		for (Product product : finalStock) {
			

			FileOutputStream fs;

			try {
				

				fs = new FileOutputStream("output.txt", true);
				PrintWriter out = new PrintWriter(fs);

				out.append("product\t\t" + product.getName() + "\n");

				try {
					out.append("boughton\t"
							+ sdf.format(product.getBoughtOn())
							+ "\n");
					if (product.getUseBy().compareTo(
							sdf.parse("01-01-1000")) != 0) {
						out.append("useby\t\t"
								+ sdf.format(product.getUseBy())
								+ "\n");
					}
				} catch (ParseException e) {
//					System.out.println(e);
				}
				out.append("boughtat\t" + "$"
						+ product.getBoughtAt() + "\n");
				out.append("quantity\t"
						+ product.getInQuantity() + "\n\n");

				out.close();
				fs.close();
			} catch (Exception e) {
				 
//				System.out.println(e);
			}
			
}

	}
	
	
	
	public void profit_query(String dateS, String dateE) {
	
//		System.out.println("print!! lose and profit");
		
		DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		profit = 0;
		lose = 0;
		
		outD = sortD(outD);
		
		if (!outS.isEmpty()) {
			for (Sell sold : outS) {
				try {
					if (sold.getSoldOn().after(sdf.parse(dateS))
							&& sold.getSoldOn().before(sdf.parse(dateE))
							|| sold.getSoldOn().equals(sdf.parse(dateS))
							|| sold.getSoldOn().equals(dateE)) {
						profit = profit + sold.getNetIncome();
					}
				} catch (ParseException e) {
					 
//					System.out.println(e);
				}

			}
//			System.out.println("print!! lose and profit");
		}
		
		if (!outD.isEmpty()) {
			for (Product disc : outD) {
				try {

						lose = lose + disc.getBoughtAt()*disc.getInQuantity();

				} catch (Exception e) {
					 
//					System.out.println(e);
				}

			}
//			System.out.println("print!! lose and profit");
		}
		
		try{
			DecimalFormat twoDForm = new DecimalFormat("#.##");
			profit= Double.valueOf(twoDForm.format(profit));
			lose= Double.valueOf(twoDForm.format(lose));
			FileOutputStream fs = new FileOutputStream("report.txt", true);
			PrintWriter out = new PrintWriter(fs);
			out.append("query profit "+ dateS + " "+ dateE+ "\n");
		

			out.append("Net income+$" + profit + "\n");

			out.append("Loss-$" + lose + "\n");
			if(lose>0){
			out.append("Discarded items in descending order of costs\n");
			}
		//****************************************************
			for (Product product : outD) {
				


					out.append("product\t\t" + product.getName() + "\n");
					out.append("boughton\t"
								+ sdf.format(product.getBoughtOn())
								+ "\n");
						if (product.getUseBy().compareTo(
								sdf.parse("01-01-1000")) != 0) {
							out.append("useby\t\t"
									+ sdf.format(product.getUseBy())
									+ "\n");
						}

					out.append("boughtat\t" + "$"
							+ product.getBoughtAt() + "\n");
					out.append("quantity\t"
							+ product.getInQuantity() + "\n");

			
			}

			out.append("\n");
			
			//*********************************************
			out.close();
		}
		catch (Exception e) {
			 
//			System.out.println(e);
		}

		
	}
	public void bestSales(String dateS, String dateE)  {

		DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		profit = 0;
		if (!outS.isEmpty()) {
			double roa = 0;
			double temp = 0;
			String bestName = null;
			for (Sell sold : outS) {
				try {
					if (sold.getSoldOn().after(sdf.parse(dateS))
							&& sold.getSoldOn().before(sdf.parse(dateE))
							|| sold.getSoldOn().equals(sdf.parse(dateS))
							|| sold.getSoldOn().equals(dateE)) {
						temp = sold.getNetIncome()/(sold.getBeforeQuantity()-sold.getOutQuantity());
						if(temp>roa){
							roa = temp;
						bestName = sold.getName();
						}
					}
				} catch (ParseException e) {
					 
//					System.out.println(e);
				}

			}
			
			try{
		        DecimalFormat twoDForm = new DecimalFormat("#.##");
		        profit= Double.valueOf(twoDForm.format(profit));
				FileOutputStream fs = new FileOutputStream("report.txt", true);
				PrintWriter out = new PrintWriter(fs);
				out.append("query bestsales "+ dateS + " "+ dateE+ "\n");
				

				out.append("Best sale: " + bestName + "\n\n");
				out.close();
				}
				catch (Exception e) {
					 
					System.out.println(e);
				}

		}
		else{
			
			try{
		        DecimalFormat twoDForm = new DecimalFormat("#.##");
		        profit= Double.valueOf(twoDForm.format(profit));
				FileOutputStream fs = new FileOutputStream("report.txt", true);
				PrintWriter out = new PrintWriter(fs);
				out.append("query bestsales"+ dateS + " "+ dateE+ "\n");
				

				out.append("Nothing sold in this period\n\n");
				out.close();
				}
				catch (Exception e) {
					 
//					System.out.println(e);
				}
			
			
		}
		

		
		
		
	}
	public void worstSales(String dateS, String dateE)  {
		
		DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		profit = 0;
		if (!outS.isEmpty()) {
			double roa = 100;
			double temp = 0;
			String bestName = null;
			for (Sell sold : outS) {
				try {
					if (sold.getSoldOn().after(sdf.parse(dateS))
							&& sold.getSoldOn().before(sdf.parse(dateE))
							|| sold.getSoldOn().equals(sdf.parse(dateS))
							|| sold.getSoldOn().equals(dateE)) {
						temp = sold.getNetIncome()/(sold.getBeforeQuantity()-sold.getOutQuantity());
						if(temp<roa){
							roa = temp;
						bestName = sold.getName();
						}
					}
				} catch (ParseException e) {
					 
//					System.out.println(e);
				}

			}
			
			try{
		        DecimalFormat twoDForm = new DecimalFormat("#.##");
		        profit= Double.valueOf(twoDForm.format(profit));
				FileOutputStream fs = new FileOutputStream("report.txt", true);
				PrintWriter out = new PrintWriter(fs);
				out.append("query worstsales "+ dateS + " "+ dateE+ "\n");
				

				out.append("Worst sale: " + bestName + "\n\n");
				out.close();
				}
				catch (Exception e) {
					 
//					System.out.println(e);
				}

		}
		else{
			
			try{
		        DecimalFormat twoDForm = new DecimalFormat("#.##");
		        profit= Double.valueOf(twoDForm.format(profit));
				FileOutputStream fs = new FileOutputStream("report.txt", true);
				PrintWriter out = new PrintWriter(fs);
				out.append("query bestsales"+ dateS + " "+ dateE+ "\n");
				

				out.append("Nothing sold in this period\n\n");
				out.close();
				}
				catch (Exception e) {
					 
//					System.out.println(e);
				}
			
			
		}

	}
	public void stock_query(String date, ArrayList<Product> report) {
		
		


		
		
		DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//		Product productBack = new Product();
		Date testD = null;

		try {
			testD = sdf.parse("01-01-1000");

		}
		catch (ParseException e) {
//			e.printStackTrace();
		}
		Date checkDate = null;
		try {
			checkDate = sdf.parse(date);
		}
		catch(Exception e) {
//			System.out.println(e);
		}

		try {
			FileOutputStream fs = new FileOutputStream("report.txt", true);
			PrintWriter out = new PrintWriter(fs);
			out.append("query "+ date + "\n");
			out.close();
		}
		catch(FileNotFoundException e) {
//			System.out.println(e);
		}

		

		
		
		
//		System.out.println(orignial.size());

		for (int r = 0; r < report.size(); r++) {
			if(report.get(r).getUseBy().before(checkDate) && !report.get(r).getUseBy().equals(testD)){
				report.remove(r);
				r = -1;
//				System.out.println("!!!!!!");
			}
		}
		

		
		String tempO = null;
		ArrayList<String> had = new ArrayList<String>();
		ArrayList<String> have = new ArrayList<String>();
		ArrayList<String> suggestion = new ArrayList<String>();

		int sizeO = orignial.size();
		int sizeR = report.size();
		
		for (int u = 0; u<sizeO; u++){
			tempO = orignial.get(u).getName();
			had.add(tempO);
		}

		for (int u = 0; u<sizeR; u++){
			tempO = report.get(u).getName();
			have.add(tempO);
		}
		suggestion = new ArrayList<String>(new HashSet<String>(had));
		ArrayList<String> newList2 = new ArrayList<String>(new HashSet<String>(have));
		
		for(String k: newList2){
			suggestion.remove(k);
		}
		
		report = sort(report);
		
		for (Product product : report) {
						
						FileOutputStream fs;
						try {
							fs = new FileOutputStream("report.txt", true);
							PrintWriter out = new PrintWriter(fs);
							out.append("product\t\t" + product.getName() + "\n");

							try {
								out.append("boughton\t"
										+ sdf.format(product.getBoughtOn())
										+ "\n");
								if (product.getUseBy().compareTo(
										sdf.parse("01-01-1000")) != 0) {
									out.append("useby\t\t"
											+ sdf.format(product.getUseBy())
											+ "\n");
								}
							} catch (ParseException e) {
//								System.out.println(e);
							}
							out.append("boughtat\t" + "$"
									+ product.getBoughtAt() + "\n");
							out.append("quantity\t"
									+ product.getInQuantity() + "\n\n");
							out.close();

						} catch (FileNotFoundException e) {
							 
//							System.out.println(e);
						}
		}
		

		FileOutputStream fs;
		try {
			fs = new FileOutputStream("report.txt", true);
			PrintWriter out = new PrintWriter(fs);
			if(!suggestion.isEmpty()){
				out.append("Suggestion on purchasing products\n");
			}
			for(String k: suggestion){
				out.append(k + "\n");
			}
			out.append("\n");
			out.close();
		} catch (Exception e) {
//				System.out.println(e);
		}
			
//		for(Product p: report){
////			System.out.println("After!productList" + p.getUseBy() + p.getName() + p.getInQuantity());
//		}
//		
//		
//		for(Product p: orignial){
////			System.out.println("After!productList" + p.getUseBy() + p.getName() + p.getInQuantity());
//		}
//		
//		for(Product p: outS){
////			System.out.println("After!productList" + p.getUseBy() + p.getName() + p.getInQuantity());
//		}
	}
	
	

	public static void main(String[] args) {
		ESM esm = new ESM();
		try{
			esm.readInstruction(args[1] + ".txt", esm.readProduct(args[0] + ".txt"));
			System.out.println("Sucessful! Please check output.txt and report.txt");
		}catch(Exception e){
			System.out.println("Invalid input!");
		}
	}
}
