export type ReactionStatus =
  | "Prepared"
  | "InProgress"
  | "Paused"
  | "Unstable"
  | "Completed"
  | "Cancelled";

export interface Reactant {
  name: string;
  quantity: number;
  unit: string;
}

export interface ReactionCondition {
  temperatureCelsius: number;
  pressureAtm: number;
  productConcentrationPercentage: number;
}

export interface ReactionMeasurement extends ReactionCondition {
  recordedAt: string;
}

export interface Reaction {
  id: string;
  name: string;
  status: ReactionStatus;
  message: string;
  catalyst: string | null;
  currentCondition: ReactionCondition;
  reactants: Reactant[];
  measurements: ReactionMeasurement[];
}
