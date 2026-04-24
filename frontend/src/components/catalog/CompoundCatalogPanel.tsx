import { useState, type FormEvent } from "react";
import type { CompoundDetails, CompoundSummary } from "../../types/catalog";

interface CompoundCatalogPanelProps {
  compounds: CompoundSummary[];
  selectedCompound: CompoundDetails | null;
  pending: boolean;
  detailsPending: boolean;
  error: string | null;
  onSearch: (query: string) => Promise<void>;
  onSelectCompound: (externalId: string) => Promise<void>;
}

function sourceLabel(source: CompoundSummary["source"]): string {
  return source === "LOCAL" ? "Local" : "PubChem";
}

function roleLabel(role: CompoundSummary["primaryRole"]): string | null {
  if (role === null) {
    return null;
  }

  return role.charAt(0) + role.slice(1).toLowerCase();
}

export function CompoundCatalogPanel({
  compounds,
  selectedCompound,
  pending,
  detailsPending,
  error,
  onSearch,
  onSelectCompound,
}: CompoundCatalogPanelProps) {
  const [query, setQuery] = useState("");

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    await onSearch(query);
  }

  return (
    <section className="panel-section">
      <div className="section-heading">
        <h3>Compound catalog</h3>
        <p>
          Search compounds with optional PubChem lookup. When the external API is unavailable,
          the backend automatically falls back to the local educational catalog.
        </p>
      </div>

      <form className="catalog-search" onSubmit={handleSubmit}>
        <div className="field field--full">
          <label htmlFor="compound-query">Compound name</label>
          <input
            id="compound-query"
            value={query}
            onChange={(event) => setQuery(event.target.value)}
            placeholder="Acetic Acid, Ethanol, Water..."
            disabled={pending}
          />
        </div>

        <div className="action-row">
          <button className="button button--primary" type="submit" disabled={pending}>
            {pending ? "Searching..." : "Search compounds"}
          </button>
          <button
            className="button button--secondary"
            type="button"
            disabled={pending}
            onClick={() => onSearch("")}
          >
            Show local catalog
          </button>
        </div>
      </form>

      {error && <div className="banner">{error}</div>}

      <div className="catalog-layout">
        <div className="catalog-results">
          {compounds.length === 0 ? (
            <p className="empty-block">No compounds available for the current query.</p>
          ) : (
            <ul className="catalog-list">
              {compounds.map((compound) => (
                <li key={compound.externalId}>
                  <button
                    className="catalog-result"
                    type="button"
                    onClick={() => onSelectCompound(compound.externalId)}
                    disabled={detailsPending}
                  >
                    <div className="catalog-result__top">
                      <strong>{compound.name}</strong>
                      <span>{compound.molecularFormula}</span>
                    </div>
                    <div className="catalog-badges">
                      <span className={`pill pill--source-${compound.source.toLowerCase()}`}>
                        {sourceLabel(compound.source)}
                      </span>
                      {roleLabel(compound.primaryRole) && (
                        <span className="pill pill--neutral">{roleLabel(compound.primaryRole)}</span>
                      )}
                      <span className="pill pill--neutral">
                        {compound.molecularWeight.toFixed(2)} g/mol
                      </span>
                    </div>
                  </button>
                </li>
              ))}
            </ul>
          )}
        </div>

        <div className="catalog-detail">
          {detailsPending ? (
            <p className="empty-block">Loading compound details...</p>
          ) : selectedCompound ? (
            <>
              <div className="section-heading">
                <h3>{selectedCompound.name}</h3>
                <p>
                  Formula {selectedCompound.molecularFormula} with molecular weight{" "}
                  {selectedCompound.molecularWeight.toFixed(2)} g/mol.
                </p>
              </div>

              <div className="catalog-badges">
                <span className={`pill pill--source-${selectedCompound.source.toLowerCase()}`}>
                  {sourceLabel(selectedCompound.source)}
                </span>
                {roleLabel(selectedCompound.primaryRole) && (
                  <span className="pill pill--neutral">{roleLabel(selectedCompound.primaryRole)}</span>
                )}
              </div>

              <div className="detail-group">
                <strong>Synonyms</strong>
                {selectedCompound.synonyms.length === 0 ? (
                  <p className="muted-copy">No synonyms available.</p>
                ) : (
                  <ul className="detail-list">
                    {selectedCompound.synonyms.map((synonym) => (
                      <li key={synonym}>{synonym}</li>
                    ))}
                  </ul>
                )}
              </div>

              <div className="detail-group">
                <strong>Identifiers</strong>
                <ul className="detail-list">
                  {selectedCompound.identifiers.map((identifier) => (
                    <li key={`${identifier.label}-${identifier.value}`}>
                      <span>{identifier.label}</span>
                      <strong>{identifier.value}</strong>
                    </li>
                  ))}
                </ul>
              </div>
            </>
          ) : (
            <p className="empty-block">
              Select a compound from the search results to inspect its synonyms and identifiers.
            </p>
          )}
        </div>
      </div>
    </section>
  );
}
