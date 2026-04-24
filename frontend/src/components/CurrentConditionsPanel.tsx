import { useState, type FormEvent } from "react";
import type { UpdateConditionPayload } from "../types/api";
import type { Reaction } from "../types/reaction";

interface CurrentConditionsPanelProps {
  reaction: Reaction;
  pending: boolean;
  onUpdateCondition: (payload: UpdateConditionPayload) => Promise<void>;
}

export function CurrentConditionsPanel({
  reaction,
  pending,
  onUpdateCondition,
}: CurrentConditionsPanelProps) {
  const [temperatureCelsius, setTemperatureCelsius] = useState(
    String(reaction.currentCondition.temperatureCelsius),
  );
  const [pressureAtm, setPressureAtm] = useState(String(reaction.currentCondition.pressureAtm));
  const [productConcentrationPercentage, setProductConcentrationPercentage] = useState(
    String(reaction.currentCondition.productConcentrationPercentage),
  );
  const [catalyst, setCatalyst] = useState(reaction.catalyst ?? "");

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    await onUpdateCondition({
      temperatureCelsius: Number(temperatureCelsius),
      pressureAtm: Number(pressureAtm),
      productConcentrationPercentage: Number(productConcentrationPercentage),
      catalyst,
    });
  }

  return (
    <section className="panel-section">
      <div className="section-heading">
        <h3>Current conditions</h3>
        <p>Read the latest operating values and adjust the prepared setup when the execution has not started.</p>
      </div>

      <div className="metric-row">
        <div className="metric">
          <span>Temperature</span>
          <strong>{reaction.currentCondition.temperatureCelsius.toFixed(1)} deg C</strong>
        </div>
        <div className="metric">
          <span>Pressure</span>
          <strong>{reaction.currentCondition.pressureAtm.toFixed(1)} atm</strong>
        </div>
        <div className="metric">
          <span>Concentration</span>
          <strong>{reaction.currentCondition.productConcentrationPercentage.toFixed(1)}%</strong>
        </div>
      </div>

      <div className="metric">
        <span>Catalyst</span>
        <strong>{reaction.catalyst || "No catalyst set"}</strong>
      </div>

      {reaction.status === "Prepared" ? (
        <form onSubmit={handleSubmit}>
          <div className="form-grid">
            <div className="field field--full">
              <label htmlFor="prepared-catalyst">Catalyst</label>
              <input
                id="prepared-catalyst"
                value={catalyst}
                onChange={(event) => setCatalyst(event.target.value)}
                placeholder="Optional catalyst"
                disabled={pending}
              />
            </div>

            <div className="field">
              <label htmlFor="prepared-temperature">Temperature (deg C)</label>
              <input
                id="prepared-temperature"
                type="number"
                step="0.1"
                value={temperatureCelsius}
                onChange={(event) => setTemperatureCelsius(event.target.value)}
                required
                disabled={pending}
              />
            </div>

            <div className="field">
              <label htmlFor="prepared-pressure">Pressure (atm)</label>
              <input
                id="prepared-pressure"
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
              <label htmlFor="prepared-concentration">Concentration (%)</label>
              <input
                id="prepared-concentration"
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
            <button className="button button--secondary" type="submit" disabled={pending}>
              {pending ? "Saving..." : "Save prepared conditions"}
            </button>
          </div>
        </form>
      ) : (
        <div className="status-note">
          Initial conditions are locked because the reaction has already left the prepared phase.
        </div>
      )}
    </section>
  );
}
