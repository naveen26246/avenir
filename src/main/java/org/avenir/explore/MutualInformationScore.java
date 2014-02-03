/*
 * avenir: Predictive analytic based on Hadoop Map Reduce
 * Author: Pranab Ghosh
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0 
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package org.avenir.explore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.chombo.util.Pair;
import org.chombo.util.Triplet;

/**
 * Processes mutual info score
 * @author pranab
 *
 */
public class MutualInformationScore {
	private List<FeatureMutualInfo>  featureClassMutualInfoList = new ArrayList<FeatureMutualInfo>();
	private List<FeaturePairMutualInfo> featurePairMutualInfoList = new ArrayList<FeaturePairMutualInfo>();
	
	/**
	 * @author pranab
	 *
	 */
	public  static class FeatureMutualInfo extends  Pair<Integer, Double>  implements  Comparable<FeatureMutualInfo> {
		public FeatureMutualInfo( int featureOrdinal, double mutualInfo) {
			super( featureOrdinal, mutualInfo);
		}
		
		@Override
		public int compareTo(FeatureMutualInfo that) {
			return that.getRight().compareTo(this.getRight());
		}
	}
	
	/**
	 * @author pranab
	 *
	 */
	public static class FeaturePairMutualInfo extends Triplet<Integer, Integer, Double> {
		public FeaturePairMutualInfo(int firstFeatureOrdinal, int secondFeatureOrdinal, double mutualInfo) {
			super(firstFeatureOrdinal, secondFeatureOrdinal, mutualInfo);
		}
	}
	
	/**
	 * @param featureOrdinal
	 * @param mutualInfo
	 */
	public void addFeatureClassMutualInfo(int featureOrdinal, double mutualInfo) {
		FeatureMutualInfo featureClassMutualInfo = new FeatureMutualInfo( featureOrdinal, mutualInfo);
		featureClassMutualInfoList.add(featureClassMutualInfo);
	}
	
	/**
	 * 
	 */
	public void sortFeatureMutualInfo() {
		Collections.sort(featureClassMutualInfoList);
	}

	/**
	 * Mutua lInformation Maximization (MIM)
	 * @return
	 */
	public List<FeatureMutualInfo> getMutualInfoMaximizer() {
		sortFeatureMutualInfo();
		return featureClassMutualInfoList;
	}

	/**
	 * @param featureOrdinal
	 * @param mutualInfo
	 */
	public void addFeaturePairMutualInfo(int firstFeatureOrdinal, int secondFeatureOrdinal, double mutualInfo) {
		FeaturePairMutualInfo featurepairMutualInfo = new FeaturePairMutualInfo( firstFeatureOrdinal, secondFeatureOrdinal, mutualInfo);
		featurePairMutualInfoList.add(featurepairMutualInfo);
	}
	
	/**
	 * Mutual Information Feature Selection (MIFS)
	 * @return
	 */
	public List<FeatureMutualInfo> getMutualInfoFeatureSelection(double redunacyFactor) {
		List<FeatureMutualInfo>  mutualInfoFeatureSelection = new ArrayList<FeatureMutualInfo>();
		
		for (FeatureMutualInfo muInfo :  featureClassMutualInfoList) {
			int feature = muInfo.getLeft();
			double sum = 0;
			for (FeaturePairMutualInfo  otherMuInfo :  featurePairMutualInfoList) {
				if (otherMuInfo.getLeft() == feature || otherMuInfo.getCenter() == feature) {
					sum +=  otherMuInfo.getRight();
				}
			}
			double score = muInfo.getRight() - redunacyFactor * sum;
			FeatureMutualInfo featureClassMutualInfo = new FeatureMutualInfo( feature,score);
			mutualInfoFeatureSelection.add(featureClassMutualInfo);
		}
		Collections.sort(mutualInfoFeatureSelection);
		return mutualInfoFeatureSelection;
	}
	
	
}