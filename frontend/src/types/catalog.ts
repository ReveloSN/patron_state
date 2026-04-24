export type CompoundSource = "LOCAL" | "PUBCHEM";
export type CompoundRole = "REACTANT" | "PRODUCT" | "CATALYST" | null;

export interface CompoundSummary {
  externalId: string;
  name: string;
  molecularFormula: string;
  molecularWeight: number;
  source: CompoundSource;
  primaryRole: CompoundRole;
}

export interface CompoundIdentifier {
  label: string;
  value: string;
}

export interface CompoundDetails extends CompoundSummary {
  synonyms: string[];
  identifiers: CompoundIdentifier[];
}

export interface ReactionRecipe {
  name: string;
  summary: string;
  reactants: string[];
  products: string[];
  catalyst: string | null;
}
