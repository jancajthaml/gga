package task10;

import java.util.HashSet;
import java.util.LinkedList;

import task10.api.algo.DijkstraShortestPath;
import task10.api.core.Edge;
import task10.api.core.Queue;
import task10.api.core.Vertex;
import task10.api.struct.GenericGraph;
import task10.api.util.Fio;

//NFA
public class Main
{

	static Fio fio						= new Fio(System.in);
	static int N						= 0;
	static int M						= 0;
	static boolean Q					= false;
	static GenericGraph<Vertex> graph	= new GenericGraph<Vertex>();
	/*
8 2 1
0 1 1    0
1 0      2 2 4
2 1 3    0
3 0      1 2
4 2 5 7    0
5 1 6    1 6
6 0      0
7 0      0
3 4 6 7
	 */

	/*
8 3 2
2 11 0 1 1 2
2 3 5
	 */

/*
8 2 1
0  1 1   0
1  0     2 2 4
2  1 3   0
3  0     1 2
4  2 5 7   0
5  1 6   1 6
6  0     0
7  0     0
4  3 4 6 7
 */
	public static void main( String ... args )
	{
		N	= fio.nextInt();	// |S|
		M	= fio.nextInt();	// |A|
		Q	= fio.nextInt()==2;	// {1,2}

		graph.ignoreConditions();
		graph.setNumberOfVerticies(N);
		
		fio.nextLine();

		long time = System.nanoTime();

		if( Q )	//Q=2 generator
		{
			int B = fio.nextInt();
			int C = fio.nextInt();
			int T = fio.nextInt();
			int U = fio.nextInt();
			int V = fio.nextInt();
			int W = fio.nextInt();
			
			fio.nextLine();
			
			double floor	= Math.floor(N/B);
			double P 		= N-floor;

			for( int sj=0; sj<N; sj++ )
			{
				for( int h=0; h<M ; h++ )
				{
					for( int k=1; k<=( T + ( (U * sj + V * h) % W ) ); k++ )
					{
						int to;

						if( sj<floor )	to = (int)(1 + sj + ((B * sj * k + C * h) % (P-1)));
						else			to = (int)(floor  + ((B * sj * k + C * h) % P));
						
						graph.addEdge(new AlphabeticalEdge(sj,to,(char)('a'+h)));
					}
				}

				graph.addVertex( new Vertex( sj ) );
			}
		}
		else	//Q=1 direct input
		{
			for(int i=0; i<N; i++)
			{
				int sj	= fio.nextInt();
				int n	= fio.nextInt();
				int j	= -1;
				
				while( ++j<n )
				{
					int to		= fio.nextInt();
					graph.addEdge(new AlphabeticalEdge(sj,to,'a'));
				}
				
				n = fio.nextInt();
				j = -1;

				while( ++j<n )
				{
					int to		= fio.nextInt();
					graph.addEdge(new AlphabeticalEdge(sj,to,'b'));
				}

				graph.addVertex( new Vertex( sj ) );
				fio.nextLine();		
			}
		}
		
		int number_of_terminals = fio.nextInt();
		
		HashSet<Integer> terminals = new HashSet<Integer>();
		for( int i=0; i<number_of_terminals; i++ )
		{
			int t						= fio.nextInt();
			terminals.add(t);
			graph.getVertex(t).terminal = true;
		}
		
		
	//Counter
		
/*
1000 10 2 
6 11 2 5 17 9
5 200 300 400 520 600
*/
		
/*
13000 3 2
2 14 1 3 11 4
1 12903
 */

		/*
11 3 2
3 10 2 1 1 2
2 6 10
		 */
		System.out.println("data loaded in "+((System.nanoTime()-time)/1000000)+"ms");
		time=System.nanoTime();
		//System.out.println(graph);
		
		graph.listenToConditions();
		

		boolean infinite = false;
		
		LinkedList<Iterable<Integer>> cycles	= graph.getSelfLoops();
		Queue[] components						= graph.getStronglyConectedComponents();
        
        test:for( int i = 0; i < M; i++ )
        {
        	if( components[i].size()>1 ) for(int v : components[i])
            {
            	if( terminals.contains(v) )
            	{
            		infinite=true;
            		break test;
            	}
            }
        }
        
		if(!infinite) for(Iterable<Integer> cycle : cycles) for(int c : cycle)
			if(terminals.contains(c)) infinite=true;
		
		System.out.println("finity detection in "+((System.nanoTime()-time)/1000000)+"ms");
		time=System.nanoTime();
		
		LinkedList<Integer> path;
		LinkedList<Integer> best = null;
		
		////
		if(infinite)
		{
			d = new DijkstraShortestPath<Vertex>(graph);
			d.execute(0);
			//System.out.println("shortest path");
		}
		else
		{
			d = new DijkstraShortestPath<Vertex>(graph);
			d.execute(0);
	//		System.out.println("longest path");
		}
		
		System.out.println("dirkstja in "+((System.nanoTime()-time)/1000000)+"ms");
		time=System.nanoTime();
		System.out.println();

		if( infinite )
		{
			for(int terminal : terminals)
			{
				path = getShortestPath(terminal);
				if( path!=null ) best=path;
			}
		}
		else
		{
			for(int terminal : terminals)
			{
				path = getLongestPath(terminal);
				if( path!=null ) best=path;
			}
		}
		
		//System.out.println("done in "+((System.nanoTime()-time)/1000000)+"ms");
		System.out.print(infinite?"INFINITE ":"FINITE ");
		if(best!=null)
		{
		//	System.out.println(best);
			for(int i=1; i<best.size(); i++)
			{
				Edge e = graph.getEdge(best.get(i-1),best.get(i));
				System.out.print(e==null?"_":e);
			}
		}
		//System.out.println();
		
		//System.out.println(best);
	}
	
	
	static DijkstraShortestPath<Vertex> d;
	static int max_path	= Integer.MIN_VALUE;
	static int min_path	= Integer.MAX_VALUE;
	
	static LinkedList<Integer> getLongestPath(int to)
	{
		LinkedList<Integer> path = d.getPathTo(to);
		if(path==null) return null;
		if( path.size()>max_path )
		{
			max_path = path.size();
			return path;
		}	
		return null;
	}

	static LinkedList<Integer> getShortestPath(int to)
	{
		LinkedList<Integer> path = d.getPathTo(to);
		if(path==null) return null;
		if( path.size()<min_path )
		{
			min_path = path.size();
			return path;
		}	
		return null;
	}
}

/*
8 2 1
0 1 1 0
1 0 2 2 4
2 1 3 0
3 0 1 2
4 2 5 7 0
5 1 6 1 6
6 0 0
7 0 0
4 3 4 6 7
*/