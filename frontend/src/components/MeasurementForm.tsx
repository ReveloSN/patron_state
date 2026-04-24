import { useState, type FormEvent } from "react";
import type { MeasurementPayload } from "../types/api";
import type { Reaction } from "../types/reaction";

interface MeasurementFormProps {
  reaction: Reaction;
  pending: boolean;
  onRecordMeasurement: (payload: MeasurementPayload) => Promise<void>;
  onStabilizeReaction: (payload: MeasurementPayload) => Promise<void>;
}

export function MeasurementForm({
  reaction,
  pending,
  onRecordMeasurement,
  onStabilizeReaction,
}: MeasurementFormProps) {
  const [temperatureCelsius, setTemperatureCelsius] = useState(
    String(reaction.currentCondition.temperatureCelsius),
  );
  const [pressureAtm, setPressureAtm] = useState(String(reaction.currentCondition.pressureAtm));
  const [productConcentrationPercentage, setProductConcentrationPercentage] = useState(
    String(reaction.currentCondition.productConcentrationPercentage),
  );

  const canRecordMeasurement = reaction.status === "InProgress";
  const canStabilize = reaction.status === "Unstable";
  const formEnabled = canRecordMeasurement || canStabilize;

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    const payload = {
      temperatureCelsius: Number(temperatureCelsius),
      pressureAtm: Number(pressureAtm),
      productConcentrationPercentage: Number(productConcentrationPercentage),
    };

    if (canStabilize) {
      await onStabilizeReaction(payload);
      return;
    }

    await onRecordMeasurement(payload);
  }

  return (
    <section className="panel-section">
      <div className="section-heading">
        <h3>{canStabilize ? "Stabilize reaction" : "Record measurement"}</h3>
        <p>
          {canStabilize
            ? "Provide safe values to bring the reaction back to the in-progress phase."
            : "Register live operating values while the reaction is running."}
        </p>
      </div>

      {formEnabled ? (
        <form onSubmit={handleSubmit}>
          <div className="form-grid">
            <div className="field">
              <label htmlFor="measurement-temperature">Temperature (deg C)</label>
              <input
                id="measurement-temperature"
                type="number"
                step="0.1"
                value={temperatureCelsius}
                onChange={(event) => setTemperatureCelsius(event.target.value)}
                required
                disabled={pending}
              />
            </div>

            <div className="field">
              <label htmlFor="measurement-pressure">Pressure (atm)</label>
              <input
                id="measurement-pressure"
                type="number"
                step="0.1"
                min="0"
                value={pressureAtm}
                onChange={(event) => setPressureAtm(event.target.value)}
                required
                disabled={pending}
              />
            </div>

            <div className="field field--full">
              <label htmlFor="measurement-concentration">Concentration (%)</label>
              <input
                id="measurement-concentration"
                type="number"
                step="0.1"
                min="0"
                max="100"
                value={productConcentrationPercentage}
                onChange={(event) => setProductConcentrationPercentage(event.target.value)}
                required
                disabled={pending}
              />
            </div>
          </div>

          <div className="action-row">
            <button className="button button--primary" type="submit" disabled={pending}>
              {pending
                ? "Saving..."
                : canStabilize
                  ? "Stabilize reaction"
                  : "Record measurement"}
            </button>
          </div>
        </form>
      ) : (
        <div className="status-note">
          Measurements are only available while the reaction is in progress, and stabilization is only available in the unstable phase.
        </div>
      )}
    </section>
  );
}
