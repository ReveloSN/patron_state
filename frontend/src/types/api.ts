export interface CreateReactionPayload {
  name: string;
  temperatureCelsius: number;
  pressureAtm: number;
  productConcentrationPercentage: number;
  catalyst: string;
}

export interface AddReactantPayload {
  name: string;
  quantity: number;
  unit: string;
}

export interface UpdateConditionPayload {
  temperatureCelsius: number;
  pressureAtm: number;
  productConcentrationPercentage: number;
  catalyst: string;
}

export interface MeasurementPayload {
  temperatureCelsius: number;
  pressureAtm: number;
  productConcentrationPercentage: number;
}

export interface ApiErrorResponse {
  message?: string;
  error?: string;
}
