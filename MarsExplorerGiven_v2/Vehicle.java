import java.util.ArrayList;
class Vehicle extends Entity{
	public boolean carryingSample;

	public Vehicle(Location l){
		super(l);
		this.carryingSample = false;
	}

	public void act(Field f, Mothership m, ArrayList<Rock> rocksCollected)
	{
		//actSimple(f,m,rocksCollected);
		actCollaborative(f,m,rocksCollected);
	}
	
	//The code dealing with agents acting collaboratively
	public void actCollaborative(Field f, Mothership m, ArrayList<Rock> rocksCollected){

		//If carrying a sample and at base, then drop sample
		if (carryingSample && f.isNeighbourTo(this.location, Mothership.class)) {
			this.carryingSample = false;
			return;
		}

		//If carrying a sample and not at the base then drop two crumbs and travel up gradient
		if (this.carryingSample) {
			f.dropCrumbs(this.location, 2);
			Location temp = new Location(this.getLocation().getRow(), this.getLocation().getCol());
			for (Location location : f.getAllfreeAdjacentLocations(this.location)) {
				if (f.getSignalStrength(location) >  f.getSignalStrength(temp)) {
					temp = location;
				}
			}
			moveTo(f, temp);
			return;
		}



		//If detect a sample then pick sample
		if (f.isNeighbourTo(this.location, Rock.class)) {
			Location rockLoc = f.getNeighbour(this.location, Rock.class);
			this.carryingSample = true;
			Rock rock = (Rock) f.getObjectAt(rockLoc);
			rocksCollected.add(rock);
			f.clearLocation(rockLoc);
			return;
		}

		//If sense crumbs then pick up one crumb and travel down gradient
		if (f.getCrumbQuantityAt(this.location) > 0) {
			f.pickUpACrumb(this.location);
			Location temp = new Location(this.getLocation().getRow(), this.getLocation().getCol());
			for (Location location : f.getAllfreeAdjacentLocations(this.location)) {
				if (f.getSignalStrength(location) <  f.getSignalStrength(temp) && f.getCrumbQuantityAt(location ) > 0) {
					temp = location;
				}
			}


			moveTo(f, temp);
			return;
		}

		//If true then move random
		if (f.getAllfreeAdjacentLocations(this.location) != null) {{
			moveTo(f, f.freeAdjacentLocation(this.location));
		}}


	}


	//The code dealing with agents acting independently
	public void actSimple(Field f, Mothership m, ArrayList<Rock> rocksCollected){
		//If detect an obstacle, change direction


		//If carrying a sample and at base, then drop sample
		if (carryingSample && f.isNeighbourTo(this.location, Mothership.class)) {
			this.carryingSample = false;
			return;
		}


		//If carrying a sample and not at the best then travel up gradient
		if (this.carryingSample) {
			//m.emitSignal(f);
			Location temp = new Location(this.getLocation().getRow(), this.getLocation().getCol());
			for (Location location : f.getAllfreeAdjacentLocations(this.location)) {
				if (f.getSignalStrength(location) >  f.getSignalStrength(temp)) {
					temp = location;
				}

			}
			moveTo(f, temp);
			return;
		}


		//If detect a sample then pick sample
		if (f.isNeighbourTo(this.location, Rock.class)) {
			Location rockLoc = f.getNeighbour(this.location, Rock.class);
			this.carryingSample = true;
			Rock rock = (Rock) f.getObjectAt(rockLoc);
			rocksCollected.add(rock);
			f.clearLocation(rockLoc);
			return;
		}

		//If true then move random
		if (f.getAllfreeAdjacentLocations(this.location) != null) {{
			moveTo(f, f.freeAdjacentLocation(this.location));
		}}




	}


	public void moveTo(Field f, Location loc)
	{
		f.clearLocation(location);
		f.place(this,loc);
		this.setLocation(loc);
	}

}
