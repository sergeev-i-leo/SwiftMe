//
//  FirstView.swift
//  java_franca_applications
//
//  Created by admin on 12.06.2026.
//

import SwiftUI

struct FirstView: View {
  var body: some View {
    ZStack {
      Color.black.ignoresSafeArea()
      Text("Первая вьюшка")
        .font(.largeTitle)
        .foregroundColor(.white)
    }
  }
}

#Preview {
  FirstView()
}
